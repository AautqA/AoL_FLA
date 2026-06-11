package com.dinereserve.util;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class HttpUtils {
    private HttpUtils() {
    }

    public static void addCorsHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
    }

    public static void handleOptions(HttpExchange exchange) throws IOException {
        addCorsHeaders(exchange);
        exchange.sendResponseHeaders(204, -1);
        exchange.close();
    }

    public static String readBody(HttpExchange exchange) throws IOException {
        try (InputStream inputStream = exchange.getRequestBody()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    public static Map<String, String> queryParams(URI uri) {
        Map<String, String> params = new LinkedHashMap<>();
        String query = uri.getRawQuery();
        if (query == null || query.isBlank()) {
            return params;
        }
        String[] pairs = query.split("&");
        for (String pair : pairs) {
            String[] parts = pair.split("=", 2);
            String key = decode(parts[0]);
            String value = parts.length > 1 ? decode(parts[1]) : "";
            params.put(key, value);
        }
        return params;
    }

    public static List<String> pathSegments(URI uri) {
        return List.of(uri.getPath().split("/"));
    }

    public static String firstSegmentAfterPrefix(String prefix, URI uri) {
        String path = uri.getPath();
        if (!path.startsWith(prefix)) {
            return "";
        }
        String remaining = path.substring(prefix.length());
        if (remaining.startsWith("/")) {
            remaining = remaining.substring(1);
        }
        int slashIndex = remaining.indexOf('/');
        if (slashIndex >= 0) {
            return remaining.substring(0, slashIndex);
        }
        return remaining;
    }

    public static String remainingPath(String prefix, URI uri) {
        String path = uri.getPath();
        if (!path.startsWith(prefix)) {
            return "";
        }
        String remaining = path.substring(prefix.length());
        return remaining.startsWith("/") ? remaining.substring(1) : remaining;
    }

    public static void sendJson(HttpExchange exchange, int statusCode, String json) throws IOException {
        addCorsHeaders(exchange);
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(statusCode, bytes.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(bytes);
        }
        exchange.close();
    }

    public static void sendError(HttpExchange exchange, int statusCode, String message) throws IOException {
        sendJson(exchange, statusCode, JsonUtils.toJson(Map.of(
                "message", message,
                "statusCode", statusCode)));
    }

    public static String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
