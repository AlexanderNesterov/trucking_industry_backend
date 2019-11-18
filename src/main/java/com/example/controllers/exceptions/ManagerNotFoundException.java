package com.example.controllers.exceptions;

public class ManagerNotFoundException extends RuntimeException {
    public ManagerNotFoundException() {
        super();
    }

    public ManagerNotFoundException(String message) {
        super(message);
    }

    public ManagerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ManagerNotFoundException(Throwable cause) {
        super(cause);
    }

    protected ManagerNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
