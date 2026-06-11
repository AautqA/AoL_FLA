package com.dinereserve.domain;

import java.util.LinkedHashMap;
import java.util.Map;

public class RestaurantTable {
    private final int tableId;
    private String restaurantName;
    private int tableNumber;
    private int capacity;
    private String status;

    public RestaurantTable(int tableId, String restaurantName, int tableNumber, int capacity, String status) {
        this.tableId = tableId;
        this.restaurantName = restaurantName;
        this.tableNumber = tableNumber;
        this.capacity = capacity;
        this.status = status;
    }

    public int getTableId() {
        return tableId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(int tableNumber) {
        this.tableNumber = tableNumber;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("table_id", tableId);
        data.put("restaurant_name", restaurantName);
        data.put("table_number", tableNumber);
        data.put("capacity", capacity);
        data.put("status", status);
        return data;
    }
}