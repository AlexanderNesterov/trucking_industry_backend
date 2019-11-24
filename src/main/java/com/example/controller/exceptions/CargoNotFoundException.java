package com.example.controller.exceptions;

public class CargoNotFoundException extends RuntimeException {
    public CargoNotFoundException() {
        super();
    }

    public CargoNotFoundException(String message) {
        super(message);
    }

    public CargoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CargoNotFoundException(Throwable cause) {
        super(cause);
    }

    protected CargoNotFoundException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
