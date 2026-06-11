package com.dinereserve.domain;

import java.util.LinkedHashMap;
import java.util.Map;

public class Reservation {
    private final int reservationId;
    private final int userId;
    private final String userName;
    private final int tableId;
    private final String restaurantName;
    private final int tableNumber;
    private final int tableCapacity;
    private final String bookingDate;
    private final String bookingTime;
    private final int peopleCount;
    private ReservationStatus status;

    public Reservation(
            int reservationId,
            int userId,
            String userName,
            int tableId,
            String restaurantName,
            int tableNumber,
            int tableCapacity,
            String bookingDate,
            String bookingTime,
            int peopleCount,
            ReservationStatus status) {
        this.reservationId = reservationId;
        this.userId = userId;
        this.userName = userName;
        this.tableId = tableId;
        this.restaurantName = restaurantName;
        this.tableNumber = tableNumber;
        this.tableCapacity = tableCapacity;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.peopleCount = peopleCount;
        this.status = status;
    }

    public int getReservationId() {
        return reservationId;
    }

    public int getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public int getTableId() {
        return tableId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public int getTableCapacity() {
        return tableCapacity;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public int getPeopleCount() {
        return peopleCount;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("reservation_id", reservationId);
        data.put("user_id", userId);
        data.put("user_name", userName);
        data.put("table_id", tableId);
        data.put("restaurant_name", restaurantName);
        data.put("table_number", tableNumber);
        data.put("table_capacity", tableCapacity);
        data.put("booking_date", bookingDate);
        data.put("booking_time", bookingTime);
        data.put("people_count", peopleCount);
        data.put("status", status.getApiValue());
        return data;
    }
}