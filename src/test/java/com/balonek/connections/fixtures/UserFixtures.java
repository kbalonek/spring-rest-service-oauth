package com.balonek.connections.fixtures;

import com.balonek.connections.domain.User;
import com.balonek.connections.domain.security.Roles;

import java.util.EnumSet;

/**
 * @author Kris Balonek
 */
public class UserFixtures {

    public static final String USER_ID = "test_user";
    public static final String ADMIN_ID = "test_admin";

    public static User userWithUserRole() {
        return new User(USER_ID, USER_ID, "spring", EnumSet.of(Roles.USER));
    }

    public static User userWithAdminRole() {
        return new User(ADMIN_ID, ADMIN_ID, "spring", EnumSet.of(Roles.ADMIN));
    }
}
