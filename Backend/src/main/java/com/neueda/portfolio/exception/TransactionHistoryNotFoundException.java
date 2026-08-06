package com.neueda.portfolio.exception;

public class TransactionHistoryNotFoundException extends RuntimeException {

    public TransactionHistoryNotFoundException(String message) {
        super(message);
    }
}