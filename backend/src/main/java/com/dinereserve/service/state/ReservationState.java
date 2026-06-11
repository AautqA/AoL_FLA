package com.dinereserve.service.state;

import com.dinereserve.domain.ReservationStatus;

public interface ReservationState {
    ReservationStatus getStatus();

    boolean canTransitionTo(ReservationStatus targetStatus);
}