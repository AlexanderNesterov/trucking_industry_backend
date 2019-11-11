package com.example.controllers.exceptions;

public class DriverExistsException extends RuntimeException {
    public DriverExistsException() {
        super();
    }

    public DriverExistsException(String message) {
        super(message);
    }

    public DriverExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public DriverExistsException(Throwable cause) {
        super(cause);
    }

    protected DriverExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
