package com.example.controller.exceptions;

public class DriverNotFoundException extends RuntimeException {
    public DriverNotFoundException() {
        super();
    }

    public DriverNotFoundException(String message) {
        super(message);
    }

    public DriverNotFoundException(Throwable cause) {
        super(cause);
    }

    public DriverNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    protected DriverNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
