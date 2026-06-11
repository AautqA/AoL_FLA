package com.dinereserve.service.state;

import com.dinereserve.domain.ReservationStatus;

public class CompletedReservationState implements ReservationState {
    @Override
    public ReservationStatus getStatus() {
        return ReservationStatus.COMPLETED;
    }

    @Override
    public boolean canTransitionTo(ReservationStatus targetStatus) {
        return false;
    }
}