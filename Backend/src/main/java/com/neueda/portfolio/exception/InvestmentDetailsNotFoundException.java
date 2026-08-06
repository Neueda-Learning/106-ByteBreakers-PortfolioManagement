package com.neueda.portfolio.exception;

public class InvestmentDetailsNotFoundException extends RuntimeException {

    public InvestmentDetailsNotFoundException(String message) {
        super(message);
    }
}