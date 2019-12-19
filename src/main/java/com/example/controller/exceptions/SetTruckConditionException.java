package com.example.controller.exceptions;

public class SetTruckConditionException extends RuntimeException {
    public SetTruckConditionException() {
        super();
    }

    public SetTruckConditionException(String message) {
        super(message);
    }

    public SetTruckConditionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SetTruckConditionException(Throwable cause) {
        super(cause);
    }

    protected SetTruckConditionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
