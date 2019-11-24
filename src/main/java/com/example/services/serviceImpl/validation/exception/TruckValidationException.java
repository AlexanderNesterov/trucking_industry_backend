package com.example.services.serviceImpl.validation.exception;

public class TruckValidationException extends RuntimeException {
    public TruckValidationException() {
        super();
    }

    public TruckValidationException(String message) {
        super(message);
    }

    public TruckValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public TruckValidationException(Throwable cause) {
        super(cause);
    }

    protected TruckValidationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
