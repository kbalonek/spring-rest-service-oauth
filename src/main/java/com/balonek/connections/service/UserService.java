package com.balonek.connections.service;

import com.balonek.connections.data.UserDao;
import com.balonek.connections.domain.User;
import com.balonek.connections.domain.security.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.EnumSet;

/**
 * @author Kris Balonek
 */
@Component
public class UserService {

    private UserDao userDao;

    @Autowired
    public UserService(UserDao userDao){
        this.userDao = userDao;
    }

    public User createUser(String username, String password) {
        // TODO: throw exception if username exists
        User user = new User(username, username, password, EnumSet.of(Roles.USER));
        return userDao.createUser(user);
    }
}
