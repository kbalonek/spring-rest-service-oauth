package com.balonek.connections.unit.data.inmemory;

import com.balonek.connections.data.UserDao;
import com.balonek.connections.data.inmemory.InMemoryUserDao;
import com.balonek.connections.domain.User;
import com.balonek.connections.domain.security.Roles;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Kris Balonek
 */
public class InMemoryUserDaoTest {

    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final EnumSet<Roles> ROLES = EnumSet.of(Roles.USER);
    public static final Set<String> CONNECTED_USER_IDS = Collections.emptySet();
    private UserDao underTest;
    private User testUser;

    @Before
    public void setUp() throws Exception {
        underTest = new InMemoryUserDao();
        testUser = new User.Builder()
                .withUserId(ID)
                .withUsername(USERNAME)
                .withPassword(PASSWORD)
                .withRoles(ROLES)
                .withConnectedUserIds(CONNECTED_USER_IDS)
                .build();
    }

    @Test
    public void should_find_user_by_username() throws Exception {
        // given
        underTest.saveUser(testUser);

        // when
        Optional<User> optionalUser = underTest.findByUsername(USERNAME);

        // then
        assertThat(optionalUser.isPresent()).isTrue();
        User user = optionalUser.get();
        verifyUser(user);
    }

    @Test
    public void should_find_user_by_user_id() throws Exception {
        // given
        underTest.saveUser(testUser);

        // when
        Optional<User> optionalUser = underTest.findByUserId(ID);

        // then
        assertThat(optionalUser.isPresent()).isTrue();
        User user = optionalUser.get();
        verifyUser(user);
    }

    @Test
    public void should_return_empty_when_there_is_no_matching_username(){
        // when
        Optional<User> optionalUser = underTest.findByUsername("no_such_user");

        // then
        assertThat(optionalUser.isPresent()).isFalse();

    }

    private void verifyUser(User user) {
        assertThat(user.getUserId()).isEqualTo(ID);
        assertThat(user.getUsername()).isEqualTo(USERNAME);
        assertThat(user.getRoles()).isEqualTo(ROLES);
        assertThat(user.getPassword()).isEqualTo(PASSWORD);
    }
}