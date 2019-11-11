package com.example.controllers.exceptions;

public class SavingCargoException extends RuntimeException {
    public SavingCargoException() {
        super();
    }

    public SavingCargoException(String message) {
        super(message);
    }

    public SavingCargoException(String message, Throwable cause) {
        super(message, cause);
    }

    public SavingCargoException(Throwable cause) {
        super(cause);
    }

    protected SavingCargoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
