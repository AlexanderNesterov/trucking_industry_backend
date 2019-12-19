package com.example.controller.exceptions;

public class SavingOrderException extends RuntimeException {
    public SavingOrderException() {
        super();
    }

    public SavingOrderException(String message) {
        super(message);
    }

    public SavingOrderException(String message, Throwable cause) {
        super(message, cause);
    }

    public SavingOrderException(Throwable cause) {
        super(cause);
    }

    protected SavingOrderException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
