package com.balonek.connections.fixtures;

import com.balonek.connections.domain.User;
import com.balonek.connections.domain.security.Roles;

import java.util.Collections;
import java.util.EnumSet;

/**
 * @author Kris Balonek
 */
public class UserFixtures {

    public static final String USER_USERNAME = "test_user";
    public static final String ADMIN_USERNAME = "test_admin";
    public static final String TEST_PASSWORD = "spring";

    public static User userWithUserRole() {
        return new User.Builder()
                .withUserId(USER_USERNAME)
                .withUsername(USER_USERNAME)
                .withPassword(TEST_PASSWORD)
                .withRoles(EnumSet.of(Roles.USER))
                .withConnectedUserIds(Collections.emptySet())
                .build();
    }

    public static User userWithAdminRole() {
        return new User.Builder()
                .withUserId(ADMIN_USERNAME)
                .withUsername(ADMIN_USERNAME)
                .withPassword(TEST_PASSWORD)
                .withRoles(EnumSet.of(Roles.ADMIN))
                .withConnectedUserIds(Collections.emptySet())
                .build();
    }
}
