package com.example.controller.exceptions;

public class BlockAccountException extends RuntimeException {
    public BlockAccountException() {
        super();
    }

    public BlockAccountException(String message) {
        super(message);
    }

    public BlockAccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public BlockAccountException(Throwable cause) {
        super(cause);
    }

    protected BlockAccountException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
