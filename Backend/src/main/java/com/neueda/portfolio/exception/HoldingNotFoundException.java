package com.neueda.portfolio.exception;

public class HoldingNotFoundException extends RuntimeException {
    public HoldingNotFoundException(String message) {
        super(message);
    }
}
