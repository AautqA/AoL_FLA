package com.dinereserve.repository;

import com.dinereserve.domain.Reservation;
import com.dinereserve.domain.ReservationStatus;
import com.dinereserve.domain.RestaurantTable;
import com.dinereserve.domain.User;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryDatabase {
    private static final InMemoryDatabase INSTANCE = new InMemoryDatabase();

    private final List<User> users = new CopyOnWriteArrayList<>();
    private final List<RestaurantTable> tables = new CopyOnWriteArrayList<>();
    private final List<Reservation> reservations = new CopyOnWriteArrayList<>();

    private final AtomicInteger userSequence = new AtomicInteger(1);
    private final AtomicInteger tableSequence = new AtomicInteger(1);
    private final AtomicInteger reservationSequence = new AtomicInteger(1);

    private InMemoryDatabase() {
        seedData();
    }

    public static InMemoryDatabase getInstance() {
        return INSTANCE;
    }

    public List<User> users() {
        return users;
    }

    public List<RestaurantTable> tables() {
        return tables;
    }

    public List<Reservation> reservations() {
        return reservations;
    }

    public int nextUserId() {
        return userSequence.getAndIncrement();
    }

    public int nextTableId() {
        return tableSequence.getAndIncrement();
    }

    public int nextReservationId() {
        return reservationSequence.getAndIncrement();
    }

    private void seedData() {
        User admin = new User(nextUserId(), "Admin User", "admin@example.com", "admin123", "admin");
        User customer = new User(nextUserId(), "Customer User", "customer@example.com", "password123", "customer");
        users.add(admin);
        users.add(customer);

        tables.add(new RestaurantTable(nextTableId(), "Savor House", 1, 2, "Available"));
        tables.add(new RestaurantTable(nextTableId(), "Savor House", 2, 4, "Available"));
        tables.add(new RestaurantTable(nextTableId(), "Ember Grill", 3, 4, "Available"));
        tables.add(new RestaurantTable(nextTableId(), "Ocean Plate", 4, 6, "Available"));
        tables.add(new RestaurantTable(nextTableId(), "Cedar Garden", 5, 6, "Available"));
        tables.add(new RestaurantTable(nextTableId(), "Cedar Garden", 6, 8, "Available"));

        reservations.add(new Reservation(
                nextReservationId(),
                customer.getUserId(),
                customer.getName(),
                1,
            "Savor House",
                1,
                2,
                "2026-05-25",
                "19:00",
                2,
                ReservationStatus.CONFIRMED));

        reservations.add(new Reservation(
                nextReservationId(),
                customer.getUserId(),
                customer.getName(),
                2,
            "Savor House",
                2,
                4,
                "2026-05-26",
                "20:00",
                4,
                ReservationStatus.CANCELLED));
    }
}