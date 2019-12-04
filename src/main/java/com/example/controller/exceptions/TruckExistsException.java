package com.example.controller.exceptions;

public class TruckExistsException extends RuntimeException {
    public TruckExistsException() {
        super();
    }

    public TruckExistsException(String message) {
        super(message);
    }

    public TruckExistsException(Throwable cause) {
        super(cause);
    }

    public TruckExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    protected TruckExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
