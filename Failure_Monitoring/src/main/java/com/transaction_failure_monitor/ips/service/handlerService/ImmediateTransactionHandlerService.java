package com.transaction_failure_monitor.ips.service.handlerService;

import com.transaction_failure_monitor.ips.dto.TransactionSummaryDTO;
import com.transaction_failure_monitor.ips.exception.TransactionNotFoundException;
import com.transaction_failure_monitor.ips.util.TransactionType;

import java.util.List;

public interface ImmediateTransactionHandlerService<T> {

    TransactionType getTransactionType();

    T findTransaction(String transactionId) throws TransactionNotFoundException;

    boolean isTransactionFailed(T transaction);

    List<T> getTodayTransactions();

    TransactionSummaryDTO buildTransactionSummary(List<T> transactions);

    String buildSuccessMessage(TransactionSummaryDTO summary);
}