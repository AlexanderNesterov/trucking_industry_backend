package com.example.controller.exceptions;

public class ChangeOrderStatusException extends RuntimeException {
    public ChangeOrderStatusException() {
        super();
    }

    public ChangeOrderStatusException(String message) {
        super(message);
    }

    public ChangeOrderStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChangeOrderStatusException(Throwable cause) {
        super(cause);
    }

    protected ChangeOrderStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
