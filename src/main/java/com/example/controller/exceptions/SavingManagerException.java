package com.example.controller.exceptions;

public class SavingManagerException extends RuntimeException {
    public SavingManagerException() {
        super();
    }

    public SavingManagerException(String message) {
        super(message);
    }

    public SavingManagerException(String message, Throwable cause) {
        super(message, cause);
    }

    public SavingManagerException(Throwable cause) {
        super(cause);
    }

    protected SavingManagerException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
