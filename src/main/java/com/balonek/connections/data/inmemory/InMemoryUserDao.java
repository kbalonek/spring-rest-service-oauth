package com.balonek.connections.data.inmemory;

import com.balonek.connections.data.UserDao;
import com.balonek.connections.domain.User;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author Kris Balonek
 */
@Repository
public class InMemoryUserDao implements UserDao {

    private Map<String, User> storage;

    public InMemoryUserDao() {
        storage = new HashMap<String, User>();
        storage.put(UserFixtures.USER_ID, UserFixtures.userWithUserRole());
        storage.put(UserFixtures.ADMIN_ID, UserFixtures.userWithAdminRole());
        storage.put(UserFixtures.USER_AND_ADMIN_ID, UserFixtures.userWithUserAndAdminRole());
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return Optional.of(storage.get(login));
    }

    @Override
    public Iterable<User> getAllUsers() {
        return storage.values();
    }
}
