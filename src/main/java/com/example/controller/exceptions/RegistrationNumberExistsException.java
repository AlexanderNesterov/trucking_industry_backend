package com.example.controller.exceptions;

public class RegistrationNumberExistsException extends RuntimeException {
    public RegistrationNumberExistsException() {
        super();
    }

    public RegistrationNumberExistsException(String message) {
        super(message);
    }

    public RegistrationNumberExistsException(Throwable cause) {
        super(cause);
    }

    public RegistrationNumberExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    protected RegistrationNumberExistsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
