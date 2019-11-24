package com.example.controller.exceptions;

public class ChangeCargoStatusException extends RuntimeException {
    public ChangeCargoStatusException() {
        super();
    }

    public ChangeCargoStatusException(String message) {
        super(message);
    }

    public ChangeCargoStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChangeCargoStatusException(Throwable cause) {
        super(cause);
    }

    protected ChangeCargoStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
