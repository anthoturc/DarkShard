package com.tavern.darkshard.model;

public enum JobStatus {
    NOT_STARTED("NOT_STARTED"),
    DONE("DONE"),
    IN_PROGRESS("IN_PROGRESS");

    private final String status;

    JobStatus(final String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }
}
