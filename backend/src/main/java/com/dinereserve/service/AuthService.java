package com.dinereserve.service;

import com.dinereserve.domain.User;
import com.dinereserve.repository.UserRepository;
import com.dinereserve.util.ApiException;

import java.util.LinkedHashMap;
import java.util.Map;

public class AuthService {
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Map<String, Object> register(String name, String email, String password) {
        if (name == null || name.isBlank() || email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new ApiException(400, "Name, email, and password are required");
        }
        userRepository.findByEmail(email).ifPresent(user -> {
            throw new ApiException(409, "Email already registered");
        });

        User user = userRepository.save(name.trim(), email.trim().toLowerCase(), password, "customer");
        return buildAuthResponse(user);
    }

    public Map<String, Object> login(String email, String password) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            throw new ApiException(400, "Email and password are required");
        }

        User user = userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new ApiException(401, "Invalid email or password"));

        if (!user.getPassword().equals(password)) {
            throw new ApiException(401, "Invalid email or password");
        }

        return buildAuthResponse(user);
    }

    private Map<String, Object> buildAuthResponse(User user) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("user", user.toPublicMap());
        response.put("token", "dinereserve-token-" + user.getUserId() + "-" + System.currentTimeMillis());
        return response;
    }
}