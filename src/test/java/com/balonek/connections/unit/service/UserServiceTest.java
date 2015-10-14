package com.balonek.connections.unit.service;

import com.balonek.connections.data.UserDao;
import com.balonek.connections.domain.User;
import com.balonek.connections.domain.exception.UserAlreadyExistsException;
import com.balonek.connections.domain.exception.UserNotFoundException;
import com.balonek.connections.domain.security.Roles;
import com.balonek.connections.fixtures.UserFixtures;
import com.balonek.connections.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

/**
 * @author Kris Balonek
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService underTest;

    private User testUser = UserFixtures.userWithUserRole();
    private User otherUser = UserFixtures.otherUserWithUserRole();

    @Test
    public void should_find_user_by_user_id() throws Exception {
        // given
        String id = "userId";
        when(userDao.findByUserId(eq(id))).thenReturn(Optional.of(testUser));

        // when
        User user = underTest.findUserByUserId(id);

        // then
        assertThat(user).isEqualTo(testUser);
    }

    @Test(expected = UserNotFoundException.class)
    public void should_throw_exception_if_user_doesnt_exist() throws Exception {
        // given
        String id = "userId";
        when(userDao.findByUserId(eq(id))).thenReturn(Optional.empty());

        // when
        underTest.findUserByUserId(id);

        // then exception should be thrown
    }

    @Test
    public void should_create_new_user() throws Exception {
        // given
        String username = "username";
        String password = "password";
        when(userDao.findByUsername(eq(username))).thenReturn(Optional.<User>empty());
        when(userDao.saveUser(any(User.class))).thenReturn(testUser);

        // when
        User user = underTest.createUser(username, password);

        // then
        assertThat(user).isEqualTo(testUser);

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userDao).saveUser(argument.capture());
        User createdByService = argument.getValue();
        assertThat(createdByService.getUserId()).isEqualTo(username);
        assertThat(createdByService.getUsername()).isEqualTo(username);
        assertThat(createdByService.getRoles()).containsOnly(Roles.USER);
    }


    /**
     * Current strategy for creating ids needs to be
     * explicitly specified in test so no one changes it accidentally.
     */
    @Test
    public void should_use_username_as_user_id() throws Exception {
        // given
        String username = "username";
        String password = "password";
        when(userDao.findByUsername(eq(username))).thenReturn(Optional.<User>empty());
        when(userDao.saveUser(any(User.class))).thenReturn(testUser);

        // when
        underTest.createUser(username, password);

        // then

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userDao).saveUser(argument.capture());
        User createdByService = argument.getValue();
        assertThat(createdByService.getUserId()).isEqualTo(username);
        assertThat(createdByService.getUsername()).isEqualTo(username);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void should_throw_exception_if_user_already_exists(){
        // given
        String username = "username";
        String password = "password";
        when(userDao.findByUsername(eq(username))).thenReturn(Optional.of(testUser));

        // when
        underTest.createUser(username, password);

        // then exception should be thrown
    }

    @Test
    public void should_add_connection() {
        // given
        when(userDao.findByUserId(eq(UserFixtures.USER_USERNAME))).thenReturn(Optional.of(testUser));
        when(userDao.findByUserId(eq(UserFixtures.OTHER_USER_USERNAME))).thenReturn(Optional.of(otherUser));
        when(userDao.saveUser(any(User.class))).thenAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            return args[0];
        });

        // when
        User updatedUser = underTest.createConnection(testUser.getUserId(), otherUser.getUserId());

        // then
        assertThat(updatedUser.getUserId()).isEqualTo(testUser.getUserId());
        assertThat(updatedUser.getUsername()).isEqualTo(testUser.getUsername());
        assertThat(updatedUser.getRoles()).isEqualTo(testUser.getRoles());
        assertThat(updatedUser.getPassword()).isEqualTo(testUser.getPassword());
        assertThat(updatedUser.getConnectedUserIds()).containsOnly(otherUser.getUserId());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userDao, times(2)).saveUser(userCaptor.capture());

        List<User> capturedUsers = userCaptor.getAllValues();

        User firstUser = capturedUsers.get(0);
        User secondUser = capturedUsers.get(1);
        assertUsersAreConnected(firstUser, secondUser);

    }

    private void assertUsersAreConnected(User firstUser, User secondUser) {
        assertThat(firstUser.getConnectedUserIds()).containsOnly(secondUser.getUserId());
        assertThat(secondUser.getConnectedUserIds()).containsOnly(firstUser.getUserId());
    }

    @Test(expected = UserNotFoundException.class)
    public void should_throw_exception_when_creating_connection_if_current_user_not_found() {
        // given
        when(userDao.findByUserId(eq(UserFixtures.USER_USERNAME))).thenReturn(Optional.empty());
        when(userDao.findByUserId(eq(UserFixtures.OTHER_USER_USERNAME))).thenReturn(Optional.of(otherUser));

        // when
        underTest.createConnection(testUser.getUserId(), otherUser.getUserId());

        // then exception should be thrown
    }

    @Test(expected = UserNotFoundException.class)
    public void should_throw_exception_when_creating_connection_if_connecting_user_not_found() {
        // given
        when(userDao.findByUserId(eq(UserFixtures.USER_USERNAME))).thenReturn(Optional.of(testUser));
        when(userDao.findByUserId(eq(UserFixtures.OTHER_USER_USERNAME))).thenReturn(Optional.empty());

        // when
        underTest.createConnection(testUser.getUserId(), otherUser.getUserId());

        // then exception should be thrown
    }
}