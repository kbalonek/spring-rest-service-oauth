package com.balonek.connections.service;

import com.balonek.connections.data.UserDao;
import com.balonek.connections.domain.User;
import com.balonek.connections.domain.security.Roles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
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
        User user = new User(ADMIN_USERNAME, ADMIN_USERNAME, ADMIN_PASSWORD, EnumSet.of(Roles.ADMIN));
        try {
            userDao.createUser(user);
        } catch (IllegalStateException ex) {
            logger.warn("Failed to create admin account", ex);
        }
    }
}
