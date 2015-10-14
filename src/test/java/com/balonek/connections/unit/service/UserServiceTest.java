package com.balonek.connections.unit.service;

import com.balonek.connections.data.UserDao;
import com.balonek.connections.domain.User;
import com.balonek.connections.domain.security.Roles;
import com.balonek.connections.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.EnumSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by kris on 10/14/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService underTest;

    @Test
    public void should_create_new_user() throws Exception {
        // given
        String username = "username";
        String password = "password";
        User dummyUser = new User("", "", "", EnumSet.allOf(Roles.class));
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
}