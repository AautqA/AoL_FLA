package com.dinereserve.service.state;

import com.dinereserve.domain.ReservationStatus;

public class CancelledReservationState implements ReservationState {
    @Override
    public ReservationStatus getStatus() {
        return ReservationStatus.CANCELLED;
    }

    @Override
    public boolean canTransitionTo(ReservationStatus targetStatus) {
        return false;
    }
}