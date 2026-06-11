package com.dinereserve.service;

import com.dinereserve.domain.RestaurantTable;
import com.dinereserve.repository.ReservationRepository;
import com.dinereserve.repository.TableRepository;
import com.dinereserve.util.ApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TableService {
    private final TableRepository tableRepository;
    private final ReservationRepository reservationRepository;

    public TableService(TableRepository tableRepository, ReservationRepository reservationRepository) {
        this.tableRepository = tableRepository;
        this.reservationRepository = reservationRepository;
    }

    public List<RestaurantTable> getAvailableTables(String date, String time, String status) {
        List<RestaurantTable> result = new ArrayList<>();
        for (RestaurantTable table : tableRepository.sortedByNumber()) {
            boolean matchesStatus = status == null || status.isBlank() || table.getStatus().equalsIgnoreCase(status);
            boolean availableForTime = true;
            if (date != null && !date.isBlank() && time != null && !time.isBlank()) {
                availableForTime = reservationRepository.findByTableAndDateTime(table.getTableId(), date, time).isEmpty();
            } else if ("Available".equalsIgnoreCase(status == null ? "Available" : status)) {
                availableForTime = "Available".equalsIgnoreCase(table.getStatus());
            }
            if (matchesStatus && availableForTime) {
                result.add(table);
            }
        }
        return result;
    }

    public List<RestaurantTable> getAllTables() {
        return tableRepository.sortedByNumber();
    }

    public RestaurantTable getTableById(int tableId) {
        return tableRepository.findById(tableId)
                .orElseThrow(() -> new ApiException(404, "Table not found"));
    }

    public RestaurantTable addTable(String restaurantName, int tableNumber, int capacity, String status) {
        if (tableNumber <= 0 || capacity <= 0) {
            throw new ApiException(400, "Table number and capacity must be greater than zero");
        }
        String resolvedStatus = (status == null || status.isBlank()) ? "Available" : status;
        String resolvedRestaurantName = (restaurantName == null || restaurantName.isBlank()) ? "DineReserve Bistro" : restaurantName;
        return tableRepository.save(resolvedRestaurantName, tableNumber, capacity, resolvedStatus);
    }

    public RestaurantTable updateTable(int tableId, Integer tableNumber, Integer capacity, String status) {
        RestaurantTable table = getTableById(tableId);
        if (tableNumber != null && tableNumber > 0) {
            table.setTableNumber(tableNumber);
        }
        if (table.getRestaurantName() == null || table.getRestaurantName().isBlank()) {
            table.setRestaurantName("DineReserve Bistro");
        }
        if (capacity != null && capacity > 0) {
            table.setCapacity(capacity);
        }
        if (status != null && !status.isBlank()) {
            table.setStatus(status);
        }
        return table;
    }

    public void deleteTable(int tableId) {
        RestaurantTable table = getTableById(tableId);
        tableRepository.delete(table);
    }

    public boolean isTableAvailable(int tableId, String date, String time) {
        getTableById(tableId);
        return reservationRepository.findByTableAndDateTime(tableId, date, time).isEmpty();
    }

    public void markTableReserved(int tableId) {
        Optional<RestaurantTable> table = tableRepository.findById(tableId);
        table.ifPresent(value -> value.setStatus("Reserved"));
    }

    public void markTableAvailable(int tableId) {
        Optional<RestaurantTable> table = tableRepository.findById(tableId);
        table.ifPresent(value -> value.setStatus("Available"));
    }
}