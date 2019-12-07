package com.example.controller.exceptions;

public class SavingCityException extends RuntimeException {
    public SavingCityException() {
        super();
    }

    public SavingCityException(String message) {
        super(message);
    }

    public SavingCityException(String message, Throwable cause) {
        super(message, cause);
    }

    public SavingCityException(Throwable cause) {
        super(cause);
    }

    protected SavingCityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
