package com.example.controller.exceptions;

public class TruckNotFoundException extends RuntimeException {
    public TruckNotFoundException() {
        super();
    }

    public TruckNotFoundException(String message) {
        super(message);
    }

    public TruckNotFoundException(Throwable cause) {
        super(cause);
    }

    public TruckNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    protected TruckNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
