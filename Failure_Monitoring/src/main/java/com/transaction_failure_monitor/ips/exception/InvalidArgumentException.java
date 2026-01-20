package com.transaction_failure_monitor.ips.exception;

public class InvalidArgumentException extends RuntimeException {
    public InvalidArgumentException(String message) {
        super(message);
    }
}
