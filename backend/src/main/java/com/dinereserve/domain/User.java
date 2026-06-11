package com.dinereserve.domain;

import java.util.LinkedHashMap;
import java.util.Map;

public class User {
    private final int userId;
    private final String name;
    private final String email;
    private final String password;
    private final String role;

    public User(int userId, String name, String email, String password, String role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public int getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public Map<String, Object> toPublicMap() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("user_id", userId);
        data.put("name", name);
        data.put("email", email);
        data.put("role", role);
        return data;
    }
}