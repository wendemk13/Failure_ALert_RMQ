package com.transaction_failure_monitor.ips.exception;


public class AlertLogServiceException extends RuntimeException {
    public AlertLogServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}