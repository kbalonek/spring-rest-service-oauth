package com.balonek.connections.unit.data.inmemory;

import com.balonek.connections.data.UserDao;
import com.balonek.connections.data.inmemory.InMemoryUserDao;
import com.balonek.connections.domain.User;
import com.balonek.connections.domain.security.Roles;
import org.junit.Before;
import org.junit.Test;

import java.util.EnumSet;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Kris Balonek
 */
public class InMemoryUserDaoTest {

    private UserDao underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new InMemoryUserDao();
    }

    @Test
    public void should_find_user_by_username() throws Exception {
        // given
        String username = "username";
        String id = "id";
        EnumSet<Roles> roles = EnumSet.of(Roles.USER);
        String password = "password";
        underTest.createUser(new User(id, username, password, roles));

        // when
        Optional<User> optionalUser = underTest.findByUsername(username);

        // then
        assertThat(optionalUser.isPresent()).isTrue();
        User user = optionalUser.get();
        verifyUser(username, id, password, user);
    }

    @Test
    public void should_find_user_by_user_id() throws Exception {
        // given
        String username = "username";
        String id = "id";
        EnumSet<Roles> roles = EnumSet.of(Roles.USER);
        String password = "password";
        underTest.createUser(new User(id, username, password, roles));

        // when
        Optional<User> optionalUser = underTest.findByUserId(id);

        // then
        assertThat(optionalUser.isPresent()).isTrue();
        User user = optionalUser.get();
        verifyUser(username, id, password, user);
    }

    @Test
    public void should_return_empty_when_there_is_no_matching_username(){
        // when
        Optional<User> optionalUser = underTest.findByUsername("no_such_user");

        // then
        assertThat(optionalUser.isPresent()).isFalse();

    }

    private void verifyUser(String username, String id, String password, User user) {
        assertThat(user.getUserId()).isEqualTo(id);
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getRoles()).containsOnly(Roles.USER);
        assertThat(user.getPassword()).isEqualTo(password);
    }
}