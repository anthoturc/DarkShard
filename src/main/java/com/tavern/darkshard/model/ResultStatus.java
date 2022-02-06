package com.tavern.darkshard.model;

public enum ResultStatus {
    PASSED("Passed"),
    FAILED("Failed"),
    TIMEOUT("Timeout");

    private final String status;
    ResultStatus(final String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
