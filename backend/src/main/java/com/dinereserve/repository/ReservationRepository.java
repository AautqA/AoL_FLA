package com.dinereserve.repository;

import com.dinereserve.domain.Reservation;
import com.dinereserve.domain.ReservationStatus;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReservationRepository {
    private final InMemoryDatabase database;

    public ReservationRepository(InMemoryDatabase database) {
        this.database = database;
    }

    public List<Reservation> findAll() {
        return database.reservations();
    }

    public Optional<Reservation> findById(int id) {
        return database.reservations().stream().filter(reservation -> reservation.getReservationId() == id).findFirst();
    }

    public List<Reservation> findByUserId(int userId) {
        return database.reservations().stream()
                .filter(reservation -> reservation.getUserId() == userId)
                .collect(Collectors.toList());
    }

    public List<Reservation> findByTableAndDateTime(int tableId, String date, String time) {
        return database.reservations().stream()
                .filter(reservation -> reservation.getTableId() == tableId)
                .filter(reservation -> reservation.getBookingDate().equals(date))
                .filter(reservation -> reservation.getBookingTime().equals(time))
                .filter(reservation -> reservation.getStatus() == ReservationStatus.CONFIRMED)
                .collect(Collectors.toList());
    }

    public Reservation save(Reservation reservation) {
        database.reservations().add(reservation);
        return reservation;
    }

    public void remove(Reservation reservation) {
        database.reservations().remove(reservation);
    }
}