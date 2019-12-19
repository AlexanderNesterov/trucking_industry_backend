package com.example.controller.exceptions;

public class SavingDriverException extends RuntimeException {
    public SavingDriverException() {
        super();
    }

    public SavingDriverException(String message) {
        super(message);
    }

    public SavingDriverException(String message, Throwable cause) {
        super(message, cause);
    }

    public SavingDriverException(Throwable cause) {
        super(cause);
    }

    protected SavingDriverException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
