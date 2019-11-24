package com.example.services.serviceImpl.validation.exception;

public class CargoValidationException extends RuntimeException {
    public CargoValidationException() {
        super();
    }

    public CargoValidationException(String message) {
        super(message);
    }

    public CargoValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public CargoValidationException(Throwable cause) {
        super(cause);
    }

    protected CargoValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
