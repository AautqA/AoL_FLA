package com.dinereserve.service.state;

import com.dinereserve.domain.ReservationStatus;

public final class ReservationStateFactory {
    private ReservationStateFactory() {
    }

    public static ReservationState from(ReservationStatus status) {
        if (status == null) {
            return new ConfirmedReservationState();
        }
        switch (status) {
            case CONFIRMED:
                return new ConfirmedReservationState();
            case CANCELLED:
                return new CancelledReservationState();
            case COMPLETED:
                return new CompletedReservationState();
            default:
                return new ConfirmedReservationState();
        }
    }
}