package com.balonek.connections.unit.data.inmemory;

import com.balonek.connections.data.UserDao;
import com.balonek.connections.data.inmemory.InMemoryUserDao;
import com.balonek.connections.domain.User;
import com.balonek.connections.domain.security.Roles;
import com.balonek.connections.fixtures.UserFixtures;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Kris Balonek
 */
public class InMemoryUserDaoTest {

    public static final String ID = "id";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final EnumSet<Roles> ROLES = EnumSet.of(Roles.USER);
    public static final String CONNECTED_USER_ID = "other-users-id";
    public static final Set<String> CONNECTED_USER_IDS = new HashSet<>(Arrays.asList(CONNECTED_USER_ID));

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
        assertThat(user.getConnectedUserIds()).containsOnly(CONNECTED_USER_ID);
    }

    @Test
    public void should_find_all_users_with_matching_username() {
        // given
        User userWithMatchingName1 = UserFixtures.createUserWithName("containing_substring_1");
        User userWithMatchingName2 = UserFixtures.createUserWithName("containing_substring_2");
        User userWithoutMatchingName = UserFixtures.createUserWithName("other");
        underTest.saveUser(userWithMatchingName1);
        underTest.saveUser(userWithMatchingName2);
        underTest.saveUser(userWithoutMatchingName);

        // when
        Collection<User> matchingUsers = underTest.searchUsers("substring");

        // then
        assertThat(matchingUsers).containsOnly(userWithMatchingName1, userWithMatchingName2);
    }

    @Test
    public void should_return_all_users_when_search_pattern_is_empty() {
        // given
        User user1 = UserFixtures.createUserWithName("user1");
        User user2 = UserFixtures.createUserWithName("user2");
        underTest.saveUser(user1);
        underTest.saveUser(user2);

        // when
        Collection<User> matchingUsers = underTest.searchUsers("");

        // then
        assertThat(matchingUsers).containsOnly(user1, user2);
    }
}