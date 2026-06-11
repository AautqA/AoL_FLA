package com.dinereserve.repository;

import com.dinereserve.domain.User;

import java.util.List;
import java.util.Optional;

public class UserRepository {
    private final InMemoryDatabase database;

    public UserRepository(InMemoryDatabase database) {
        this.database = database;
    }

    public List<User> findAll() {
        return database.users();
    }

    public Optional<User> findById(int id) {
        return database.users().stream().filter(user -> user.getUserId() == id).findFirst();
    }

    public Optional<User> findByEmail(String email) {
        return database.users().stream().filter(user -> user.getEmail().equalsIgnoreCase(email)).findFirst();
    }

    public User save(String name, String email, String password, String role) {
        User user = new User(database.nextUserId(), name, email, password, role);
        database.users().add(user);
        return user;
    }
}