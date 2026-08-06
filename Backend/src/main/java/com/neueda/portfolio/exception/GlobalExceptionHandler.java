package com.neueda.portfolio.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(HoldingNotFoundException.class)
    public ResponseEntity<String> handleHoldingNotFoundException(
            HoldingNotFoundException ex) {

        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }


    @ExceptionHandler(InvestmentDetailsNotFoundException.class)
    public ResponseEntity<String> handleInvestmentDetailsNotFoundException(
            InvestmentDetailsNotFoundException ex) {

        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(InvestmentOptionNotFoundException.class)
    public ResponseEntity<String> handleInvestmentOptionNotFoundException(
            InvestmentOptionNotFoundException ex) {

        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(TransactionHistoryNotFoundException.class)
    public ResponseEntity<String> handleTransactionHistoryNotFoundException(
            TransactionHistoryNotFoundException ex) {

        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.NOT_FOUND
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> genericHandleException(Exception ex) {

        return new ResponseEntity<>(
                ex.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}