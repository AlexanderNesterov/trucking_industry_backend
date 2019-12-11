package com.example.controller.exceptions;

public class SetStatusException extends RuntimeException {
    public SetStatusException() {
        super();
    }

    public SetStatusException(String message) {
        super(message);
    }

    public SetStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public SetStatusException(Throwable cause) {
        super(cause);
    }

    protected SetStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
