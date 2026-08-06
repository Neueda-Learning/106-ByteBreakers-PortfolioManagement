package com.neueda.portfolio.diversify.exception;

public class DiversifyServiceException extends RuntimeException {
    public DiversifyServiceException(String message) {
        super(message);
    }

    public DiversifyServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}

