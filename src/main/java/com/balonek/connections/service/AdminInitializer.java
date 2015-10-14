package com.balonek.connections.service;

import com.balonek.connections.data.UserDao;
import com.balonek.connections.domain.User;
import com.balonek.connections.domain.security.Roles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.EnumSet;

/**
 * @author Kris Balonek
 */
@Component
public class AdminInitializer {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final Logger logger = LoggerFactory.getLogger(AdminInitializer.class);

    private UserDao userDao;

    @Autowired
    public AdminInitializer(UserDao userDao){
        this.userDao = userDao;
    }

    @PostConstruct
    public void createAdmin() {
        User user = new User.Builder()
                .withUserId(ADMIN_USERNAME)
                .withUsername(ADMIN_USERNAME)
                .withPassword(ADMIN_PASSWORD)
                .withRoles(EnumSet.of(Roles.ADMIN))
                .withConnectedUserIds(Collections.emptySet())
                .build();
        try {
            userDao.createUser(user);
        } catch (IllegalStateException ex) {
            logger.warn("Failed to create admin account", ex);
        }
    }
}
