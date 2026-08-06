package com.neueda.portfolio.diversify.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.neueda.portfolio.diversify")
public class DiversifyExceptionHandler {

    @ExceptionHandler(DiversifyBadRequestException.class)
    public ResponseEntity<ApiErrorResponse> handleBadRequest(DiversifyBadRequestException ex,
                                                             HttpServletRequest request) {
        return build(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(DiversifyServiceException.class)
    public ResponseEntity<ApiErrorResponse> handleServiceError(DiversifyServiceException ex,
                                                               HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiErrorResponse> handleDataAccess(DataAccessException ex,
                                                             HttpServletRequest request) {
        return build(HttpStatus.SERVICE_UNAVAILABLE,
                "Database operation failed while processing diversify request.",
                request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex,
                                                          HttpServletRequest request) {
        return build(HttpStatus.INTERNAL_SERVER_ERROR,
                "Unexpected error while processing diversify request.",
                request);
    }

    private ResponseEntity<ApiErrorResponse> build(HttpStatus status, String message, HttpServletRequest request) {
        ApiErrorResponse body = new ApiErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                request.getRequestURI()
        );
        return ResponseEntity.status(status).body(body);
    }
}

