package com.balonek.connections.fixtures;

import com.balonek.connections.domain.User;
import com.balonek.connections.domain.security.Roles;

import java.util.EnumSet;

/**
 * @author Kris Balonek
 */
public class UserFixtures {

    public static final String USER_USERNAME = "test_user";
    public static final String ADMIN_USERNAME = "test_admin";
    public static final String TEST_PASSWORD = "spring";

    public static User userWithUserRole() {
        return new User(USER_USERNAME, USER_USERNAME, TEST_PASSWORD, EnumSet.of(Roles.USER));
    }

    public static User userWithAdminRole() {
        return new User(ADMIN_USERNAME, ADMIN_USERNAME, TEST_PASSWORD, EnumSet.of(Roles.ADMIN));
    }
}
