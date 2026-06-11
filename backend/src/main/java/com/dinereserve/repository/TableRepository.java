package com.dinereserve.repository;

import com.dinereserve.domain.RestaurantTable;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TableRepository {
    private final InMemoryDatabase database;

    public TableRepository(InMemoryDatabase database) {
        this.database = database;
    }

    public List<RestaurantTable> findAll() {
        return database.tables();
    }

    public Optional<RestaurantTable> findById(int id) {
        return database.tables().stream().filter(table -> table.getTableId() == id).findFirst();
    }

    public RestaurantTable save(String restaurantName, int tableNumber, int capacity, String status) {
        RestaurantTable table = new RestaurantTable(database.nextTableId(), restaurantName, tableNumber, capacity, status);
        database.tables().add(table);
        return table;
    }

    public void delete(RestaurantTable table) {
        database.tables().remove(table);
    }

    public List<RestaurantTable> sortedByNumber() {
        return database.tables().stream()
                .sorted(Comparator.comparingInt(RestaurantTable::getTableNumber))
                .collect(Collectors.toList());
    }
}