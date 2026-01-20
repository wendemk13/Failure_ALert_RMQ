package com.transaction_failure_monitor.ips.exception;

public class TransactionAlreadyAlertedException extends RuntimeException {
    public TransactionAlreadyAlertedException(String message) {
        super(message);
    }
}
