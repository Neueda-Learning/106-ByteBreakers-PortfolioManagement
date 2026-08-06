package com.neueda.portfolio.exception;

public class InvestmentOptionNotFoundException extends RuntimeException {

    public InvestmentOptionNotFoundException(String message) {
        super(message);
    }
}