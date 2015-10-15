package com.balonek.connections.data.inmemory;

import com.balonek.connections.data.UserDao;
import com.balonek.connections.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

/**
 * @author Kris Balonek
 */
@Repository
public class InMemoryUserDao implements UserDao {

    private Map<String, User> userByUsername;
    private Map<String, User> userById;

    private ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public InMemoryUserDao() {
        userByUsername = new HashMap<>();
        userById = new HashMap<>();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        readWriteLock.readLock().lock();
        try {
            return Optional.ofNullable(userByUsername.get(username));
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public Optional<User> findByUserId(String userId) {
        readWriteLock.readLock().lock();
        try {
            return Optional.ofNullable(userById.get(userId));
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public Collection<User> getAllUsers() {
        readWriteLock.readLock().lock();
        try {
            return userByUsername.values();
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public Collection<User> searchUsers(String containing) {
        readWriteLock.readLock().lock();
        try {
            return userByUsername.values().stream()
                    .filter(user -> user.getUsername().contains(containing))
                    .collect(Collectors.toSet());
        } finally {
            readWriteLock.readLock().unlock();
        }
    }

    @Override
    public User saveUser(User user) {
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
