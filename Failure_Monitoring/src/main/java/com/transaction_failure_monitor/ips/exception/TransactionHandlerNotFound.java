package com.transaction_failure_monitor.ips.exception;

public class TransactionHandlerNotFound extends RuntimeException {
    public TransactionHandlerNotFound(String message) {
        super(message);
    }
}
