package com.balonek.connections.service;

import com.balonek.connections.data.UserDao;
import com.balonek.connections.domain.User;
import com.balonek.connections.domain.exception.UserAlreadyExistsException;
import com.balonek.connections.domain.exception.UserNotFoundException;
import com.balonek.connections.domain.security.Roles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Optional;

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

    /**
     * The method represents a transaction - user can be created only if the username is not present in the
     * data store. To achieve that, the method is synchronized, but if we were using an external data
     * store and multiple web-service instances this approach wouldn't suffice. For example, if we were using
     * Amazon Dynamodb we could use its conditional write feature.
     *
     * @throws com.balonek.connections.domain.exception.UserAlreadyExistsException If username already exists
     *                                                                             in the storage.
     */
    public synchronized User createUser(String username, String password) {
        if (userDao.findByUsername(username).isPresent()) {
            throw new UserAlreadyExistsException("User already exists");
        }
        User user = new User.Builder()
                .withUserId(username)
                .withUsername(username)
                .withPassword(password)
                .withRoles(EnumSet.of(Roles.USER))
                .withConnectedUserIds(Collections.emptySet())
                .build();
        return userDao.saveUser(user);
    }

    public User findUserByUserId(String userId) {
        Optional<User> optionalUser = userDao.findByUserId(userId);
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException("User not found");
        }
        return optionalUser.get();
    }

    public synchronized User createConnection(String currentUserId, String otherUserId) {
        Optional<User> optionalCurrentUser = userDao.findByUserId(currentUserId);
        Optional<User> optionalOtherUser = userDao.findByUserId(otherUserId);
        if (!optionalCurrentUser.isPresent()) {
            throw new UserNotFoundException(String.format("User not found: %s", currentUserId));
        }
        if (!optionalOtherUser.isPresent()) {
            throw new UserNotFoundException(String.format("User not found: %s", otherUserId));
        }
        User updatedCurrentUser = optionalCurrentUser.get().withAddedConnection(otherUserId);
        User updatedOtherUser = optionalOtherUser.get().withAddedConnection(currentUserId);
        userDao.saveUser(updatedCurrentUser);
        userDao.saveUser(updatedOtherUser);
        return updatedCurrentUser;
    }
}
