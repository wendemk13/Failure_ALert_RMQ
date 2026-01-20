package com.transaction_failure_monitor.ips.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleAllExceptions(Exception ex, HttpServletRequest request) {
        CustomErrorResponse response = new CustomErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleTransactionNotFoundException(TransactionNotFoundException ex, HttpServletRequest request) {
        CustomErrorResponse response = new CustomErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransactionAlreadyAlertedException.class)
    public ResponseEntity<CustomErrorResponse> handleTransactionAlreadyAlertedException(TransactionAlreadyAlertedException ex, HttpServletRequest request) {
        CustomErrorResponse response = new CustomErrorResponse(
                HttpStatus.CONFLICT.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(SMSAlertingServiceException.class)
    public ResponseEntity<CustomErrorResponse> handleSMSAlertingServiceException(SMSAlertingServiceException ex, HttpServletRequest request) {
        CustomErrorResponse response = new CustomErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(AlertLogServiceException.class)
    public ResponseEntity<CustomErrorResponse> handleAlertLogServiceException(AlertLogServiceException ex, HttpServletRequest request) {
        CustomErrorResponse response = new CustomErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidArgumentException.class)
    public ResponseEntity<CustomErrorResponse> handleInvalidDateArgumentException(InvalidArgumentException ex, HttpServletRequest request) {
        CustomErrorResponse response = new CustomErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(TransactionHandlerNotFound.class)
    public ResponseEntity<CustomErrorResponse> handleTransactionHandlerNotFound(TransactionHandlerNotFound ex, HttpServletRequest request) {
        CustomErrorResponse response = new CustomErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
