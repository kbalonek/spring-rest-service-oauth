package com.balonek.connections.data.inmemory;

import com.balonek.connections.data.UserDao;
import com.balonek.connections.domain.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Kris Balonek
 */
@Repository
public class InMemoryUserDao implements UserDao {

    private Map<String, User> userByUsername;
    private Map<String, User> userById;

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public InMemoryUserDao() {

        userByUsername = new HashMap<String, User>();
        userById = new HashMap<String, User>();
    }

    @Override
    public Optional<User> findByUsername(String login) {
        readWriteLock.readLock().lock();
        try {
            return Optional.ofNullable(userByUsername.get(login));
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public Iterable<User> getAllUsers() {
        readWriteLock.readLock().lock();
        try {
            return userByUsername.values();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public User createUser(User user) {
        readWriteLock.writeLock().lock();
        try {
            userByUsername.put(user.getUsername(), user);
            userById.put(user.getUserId(), user);
            return user;
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }
}
