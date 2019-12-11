package com.example.controller.exceptions;

public class SavingTruckException extends RuntimeException {
    public SavingTruckException() {
        super();
    }

    public SavingTruckException(String message) {
        super(message);
    }

    public SavingTruckException(String message, Throwable cause) {
        super(message, cause);
    }

    public SavingTruckException(Throwable cause) {
        super(cause);
    }

    protected SavingTruckException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
