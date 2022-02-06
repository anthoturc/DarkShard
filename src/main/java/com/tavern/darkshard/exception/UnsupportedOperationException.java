package com.tavern.darkshard.exception;

public class UnsupportedOperationException extends RuntimeException {
    public UnsupportedOperationException(String msg) {
        super(msg);
    }

    public UnsupportedOperationException(String msg, Exception ex) {
        super(msg, ex);
    }
}
