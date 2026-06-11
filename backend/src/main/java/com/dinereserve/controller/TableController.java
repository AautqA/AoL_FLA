package com.dinereserve.controller;

import com.dinereserve.domain.RestaurantTable;
import com.dinereserve.service.TableService;
import com.dinereserve.util.ApiException;
import com.dinereserve.util.HttpUtils;
import com.dinereserve.util.JsonUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TableController implements HttpHandler {
    private final TableService tableService;

    public TableController(TableService tableService) {
        this.tableService = tableService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            HttpUtils.handleOptions(exchange);
            return;
        }

        try {
            String method = exchange.getRequestMethod();
            String remaining = HttpUtils.remainingPath("/api/tables", exchange.getRequestURI());

            if (remaining.isEmpty()) {
                if ("GET".equalsIgnoreCase(method)) {
                    Map<String, String> query = HttpUtils.queryParams(exchange.getRequestURI());
                    List<Map<String, Object>> tables = tableService.getAvailableTables(
                            query.get("date"),
                            query.get("time"),
                            query.get("status")).stream().map(RestaurantTable::toMap).collect(Collectors.toList());
                    HttpUtils.sendJson(exchange, 200, JsonUtils.toJson(tables));
                    return;
                }

                if ("POST".equalsIgnoreCase(method)) {
                    Map<String, String> body = JsonUtils.parseObject(HttpUtils.readBody(exchange));
                    RestaurantTable table = tableService.addTable(
                            body.get("restaurant_name"),
                            Integer.parseInt(body.getOrDefault("table_number", "0")),
                            Integer.parseInt(body.getOrDefault("capacity", "0")),
                            body.getOrDefault("status", "Available"));
                    HttpUtils.sendJson(exchange, 201, JsonUtils.toJson(table.toMap()));
                    return;
                }
            }

            if ("all".equalsIgnoreCase(remaining) && "GET".equalsIgnoreCase(method)) {
                List<Map<String, Object>> tables = tableService.getAllTables().stream()
                        .map(RestaurantTable::toMap)
                        .collect(Collectors.toList());
                HttpUtils.sendJson(exchange, 200, JsonUtils.toJson(tables));
                return;
            }

            if (remaining.endsWith("/availability") && "GET".equalsIgnoreCase(method)) {
                String tableIdPart = remaining.substring(0, remaining.indexOf('/'));
                int tableId = Integer.parseInt(tableIdPart);
                Map<String, String> query = HttpUtils.queryParams(exchange.getRequestURI());
                boolean available = tableService.isTableAvailable(tableId, query.get("date"), query.get("time"));
                HttpUtils.sendJson(exchange, 200, JsonUtils.toJson(Map.of(
                        "available", available,
                        "table_id", tableId,
                        "date", query.getOrDefault("date", ""),
                        "time", query.getOrDefault("time", ""))));
                return;
            }

            if (!remaining.isEmpty()) {
                String[] parts = remaining.split("/");
                int tableId = Integer.parseInt(parts[0]);

                if (parts.length == 1 && "GET".equalsIgnoreCase(method)) {
                    HttpUtils.sendJson(exchange, 200, JsonUtils.toJson(tableService.getTableById(tableId).toMap()));
                    return;
                }

                if (parts.length == 1 && "PUT".equalsIgnoreCase(method)) {
                    Map<String, String> body = JsonUtils.parseObject(HttpUtils.readBody(exchange));
                    String restaurantName = body.get("restaurant_name");
                    Integer tableNumber = body.containsKey("table_number") ? Integer.valueOf(body.get("table_number")) : null;
                    Integer capacity = body.containsKey("capacity") ? Integer.valueOf(body.get("capacity")) : null;
                    String status = body.get("status");
                    RestaurantTable table = tableService.updateTable(tableId, tableNumber, capacity, status);
                    if (restaurantName != null && !restaurantName.isBlank()) {
                        table.setRestaurantName(restaurantName);
                    }
                    HttpUtils.sendJson(exchange, 200, JsonUtils.toJson(table.toMap()));
                    return;
                }

                if (parts.length == 1 && "DELETE".equalsIgnoreCase(method)) {
                    tableService.deleteTable(tableId);
                    HttpUtils.sendJson(exchange, 200, JsonUtils.toJson(Map.of(
                            "message", "Table deleted successfully")));
                    return;
                }

                if (parts.length == 2 && "availability".equalsIgnoreCase(parts[1]) && "GET".equalsIgnoreCase(method)) {
                    Map<String, String> query = HttpUtils.queryParams(exchange.getRequestURI());
                    boolean available = tableService.isTableAvailable(tableId, query.get("date"), query.get("time"));
                    HttpUtils.sendJson(exchange, 200, JsonUtils.toJson(Map.of(
                            "available", available,
                            "table_id", tableId,
                            "date", query.getOrDefault("date", ""),
                            "time", query.getOrDefault("time", ""))));
                    return;
                }
            }

            HttpUtils.sendError(exchange, 404, "Table endpoint not found");
        } catch (ApiException exception) {
            HttpUtils.sendError(exchange, exception.getStatusCode(), exception.getMessage());
        } catch (Exception exception) {
            HttpUtils.sendError(exchange, 500, "Internal server error");
        }
    }
}