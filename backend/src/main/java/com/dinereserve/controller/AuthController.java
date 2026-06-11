package com.dinereserve.controller;

import com.dinereserve.service.AuthService;
import com.dinereserve.util.ApiException;
import com.dinereserve.util.HttpUtils;
import com.dinereserve.util.JsonUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;

public class AuthController implements HttpHandler {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            HttpUtils.handleOptions(exchange);
            return;
        }

        try {
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();

            if ("POST".equalsIgnoreCase(method) && path.endsWith("/register")) {
                Map<String, String> body = JsonUtils.parseObject(HttpUtils.readBody(exchange));
                Map<String, Object> response = authService.register(
                        body.get("name"),
                        body.get("email"),
                        body.get("password"));
                HttpUtils.sendJson(exchange, 201, JsonUtils.toJson(response));
                return;
            }

            if ("POST".equalsIgnoreCase(method) && path.endsWith("/login")) {
                Map<String, String> body = JsonUtils.parseObject(HttpUtils.readBody(exchange));
                Map<String, Object> response = authService.login(
                        body.get("email"),
                        body.get("password"));
                HttpUtils.sendJson(exchange, 200, JsonUtils.toJson(response));
                return;
            }

            HttpUtils.sendError(exchange, 404, "Auth endpoint not found");
        } catch (ApiException exception) {
            HttpUtils.sendError(exchange, exception.getStatusCode(), exception.getMessage());
        } catch (Exception exception) {
            HttpUtils.sendError(exchange, 500, "Internal server error");
        }
    }
}