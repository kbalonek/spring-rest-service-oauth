package com.balonek.connections.unit.service;

import com.balonek.connections.data.UserDao;
import com.balonek.connections.domain.User;
import com.balonek.connections.domain.exception.UserAlreadyExistsException;
import com.balonek.connections.domain.exception.UserNotFoundException;
import com.balonek.connections.domain.security.Roles;
import com.balonek.connections.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.EnumSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Kris Balonek
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService underTest;

    private User dummyUser = new User("", "", "", EnumSet.allOf(Roles.class));

    @Test
    public void should_find_user_by_user_id() throws Exception {
        // given
        String id = "userId";
        when(userDao.findByUserId(eq(id))).thenReturn(Optional.of(dummyUser));

        // when
        User user = underTest.findUserByUserId(id);

        // then
        assertThat(user).isEqualTo(dummyUser);
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
        when(userDao.createUser(any(User.class))).thenReturn(dummyUser);

        // when
        User user = underTest.createUser(username, password);

        // then
        assertThat(user).isEqualTo(dummyUser);

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userDao).createUser(argument.capture());
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
        when(userDao.createUser(any(User.class))).thenReturn(dummyUser);

        // when
        underTest.createUser(username, password);

        // then

        ArgumentCaptor<User> argument = ArgumentCaptor.forClass(User.class);
        verify(userDao).createUser(argument.capture());
        User createdByService = argument.getValue();
        assertThat(createdByService.getUserId()).isEqualTo(username);
        assertThat(createdByService.getUsername()).isEqualTo(username);
    }

    @Test(expected = UserAlreadyExistsException.class)
    public void should_throw_exception_if_user_already_exists(){
        // given
        String username = "username";
        String password = "password";
        when(userDao.findByUsername(eq(username))).thenReturn(Optional.of(dummyUser));

        // when
        underTest.createUser(username, password);

        // then exception should be thrown
    }


}