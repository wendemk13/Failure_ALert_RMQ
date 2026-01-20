package com.transaction_failure_monitor.ips.exception;

public class SMSAlertingServiceException extends RuntimeException {
    public SMSAlertingServiceException(String message) {
        super(message);
    }
}
