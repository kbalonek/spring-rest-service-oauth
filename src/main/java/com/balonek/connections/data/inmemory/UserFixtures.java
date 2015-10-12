package com.balonek.connections.data.inmemory;

import com.balonek.connections.domain.User;
import com.balonek.connections.domain.security.Role;

import java.util.EnumSet;

/**
 * @author Kris Balonek
 */
public class UserFixtures {

    public static final String USER_ID = "user";
    public static final String ADMIN_ID = "admin";
    public static final String USER_AND_ADMIN_ID = "user_and_admin";

    public static User userWithUserRole() {
        return new User(USER_ID, USER_ID, "spring", EnumSet.of(Role.USER));
    }

    public static User userWithAdminRole() {
        return new User(ADMIN_ID, ADMIN_ID, "spring", EnumSet.of(Role.ADMIN));
    }

    public static User userWithUserAndAdminRole() {
        return new User(USER_AND_ADMIN_ID, USER_AND_ADMIN_ID, "spring", EnumSet.of(Role.USER, Role.ADMIN));
    }
}
