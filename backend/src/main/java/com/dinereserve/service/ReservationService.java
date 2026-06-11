package com.dinereserve.service;

import com.dinereserve.domain.Reservation;
import com.dinereserve.domain.ReservationStatus;
import com.dinereserve.domain.RestaurantTable;
import com.dinereserve.domain.User;
import com.dinereserve.repository.ReservationRepository;
import com.dinereserve.repository.TableRepository;
import com.dinereserve.repository.UserRepository;
import com.dinereserve.service.state.ReservationState;
import com.dinereserve.service.state.ReservationStateFactory;
import com.dinereserve.util.ApiException;

import java.util.ArrayList;
import java.util.List;

public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository, TableRepository tableRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.tableRepository = tableRepository;
        this.userRepository = userRepository;
    }

    public Reservation createReservation(int userId, int tableId, String bookingDate, String bookingTime, int peopleCount) {
        if (bookingDate == null || bookingDate.isBlank() || bookingTime == null || bookingTime.isBlank()) {
            throw new ApiException(400, "Booking date and time are required");
        }
        if (peopleCount <= 0) {
            throw new ApiException(400, "People count must be greater than zero");
        }

        User user = userRepository.findById(userId).orElseThrow(() -> new ApiException(404, "User not found"));
        RestaurantTable table = tableRepository.findById(tableId).orElseThrow(() -> new ApiException(404, "Table not found"));

        if (peopleCount > table.getCapacity()) {
            throw new ApiException(400, "People count exceeds table capacity");
        }
        if (!reservationRepository.findByTableAndDateTime(tableId, bookingDate, bookingTime).isEmpty()) {
            throw new ApiException(409, "Table is already reserved for the selected date and time");
        }

        int nextReservationId = reservationRepository.findAll().stream()
            .mapToInt(Reservation::getReservationId)
            .max()
            .orElse(0) + 1;

        Reservation reservation = new Reservation(
            nextReservationId,
                user.getUserId(),
                user.getName(),
                table.getTableId(),
                table.getRestaurantName(),
                table.getTableNumber(),
                table.getCapacity(),
                bookingDate,
                bookingTime,
                peopleCount,
                ReservationStatus.CONFIRMED);

        reservationRepository.save(reservation);
        table.setStatus("Reserved");
        return reservation;
    }

    public Reservation cancelReservation(int reservationId) {
        Reservation reservation = getReservationById(reservationId);
        ReservationState currentState = ReservationStateFactory.from(reservation.getStatus());
        if (!currentState.canTransitionTo(ReservationStatus.CANCELLED)) {
            throw new ApiException(400, "Reservation cannot be cancelled from its current state");
        }
        reservation.setStatus(ReservationStatus.CANCELLED);
        tableRepository.findById(reservation.getTableId()).ifPresent(table -> table.setStatus("Available"));
        return reservation;
    }

    public List<Reservation> getReservationHistory(int userId) {
        return new ArrayList<>(reservationRepository.findByUserId(userId));
    }

    public List<Reservation> getAllReservations() {
        return new ArrayList<>(reservationRepository.findAll());
    }

    public Reservation getReservationById(int reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ApiException(404, "Reservation not found"));
    }

    public boolean checkAvailability(int tableId, String date, String time) {
        tableRepository.findById(tableId).orElseThrow(() -> new ApiException(404, "Table not found"));
        return reservationRepository.findByTableAndDateTime(tableId, date, time).isEmpty();
    }
}