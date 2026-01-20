package com.transaction_failure_monitor.ips.service.handlerService;

import com.transaction_failure_monitor.ips.dto.TransactionSummaryDTO;
import com.transaction_failure_monitor.ips.util.TransactionType;

import java.util.List;

public interface DailyTransactionHandlerService<T> {
    TransactionType getTransactionType();

    List<?> getTodayTransactions();

    TransactionSummaryDTO buildSummary(List<?> transactions);
}

