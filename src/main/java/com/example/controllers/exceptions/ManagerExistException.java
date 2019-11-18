package com.example.controllers.exceptions;

public class ManagerExistException extends RuntimeException {
    public ManagerExistException() {
        super();
    }

    public ManagerExistException(String message) {
        super(message);
    }

    public ManagerExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public ManagerExistException(Throwable cause) {
        super(cause);
    }

    protected ManagerExistException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
