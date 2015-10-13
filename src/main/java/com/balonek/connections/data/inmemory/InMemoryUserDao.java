package com.balonek.connections.data.inmemory;

import com.balonek.connections.data.UserDao;
import com.balonek.connections.domain.User;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Kris Balonek
 */
@Repository
public class InMemoryUserDao implements UserDao {

    private Map<String, User> storage;

    public InMemoryUserDao() {
        storage = new ConcurrentHashMap<String, User>();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.of(storage.get(login));
    }

    @Override
    public Iterable<User> getAllUsers() {
        return storage.values();
    }

    @Override
    public User createUser(User user) {
        storage.put(user.getUserId(), user);
        return user;
    }
}
