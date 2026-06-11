package com.dinereserve.service.state;

import com.dinereserve.domain.ReservationStatus;

public class ConfirmedReservationState implements ReservationState {
    @Override
    public ReservationStatus getStatus() {
        return ReservationStatus.CONFIRMED;
    }

    @Override
    public boolean canTransitionTo(ReservationStatus targetStatus) {
        return targetStatus == ReservationStatus.CANCELLED || targetStatus == ReservationStatus.COMPLETED;
    }
}