package com.banking.app.model;

import java.util.Arrays;

public enum Status {

    PRIMARY(1),
    SECONDARY(2);

    private final int statusCode;

    Status(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public static Status valueOf(Integer statusCode) {
        return Arrays.stream(Status.values())
                .filter(status -> status.getStatusCode() == statusCode)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Status ne odgovara: " + statusCode));
    }

}
