package com.dinereserve.domain;

public enum ReservationStatus {
    CONFIRMED("confirmed"),
    CANCELLED("cancelled"),
    COMPLETED("completed");

    private final String apiValue;

    ReservationStatus(String apiValue) {
        this.apiValue = apiValue;
    }

    public String getApiValue() {
        return apiValue;
    }

    public static ReservationStatus fromApiValue(String value) {
        if (value == null) {
            return CONFIRMED;
        }
        for (ReservationStatus status : values()) {
            if (status.apiValue.equalsIgnoreCase(value) || status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        return CONFIRMED;
    }
}