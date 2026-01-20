package com.transaction_failure_monitor.ips.serviceImpl.DailyTransactionTypeHandler;

import com.transaction_failure_monitor.ips.dto.TransactionSummaryDTO;
import com.transaction_failure_monitor.ips.entity.TelebirrTransactions;
import com.transaction_failure_monitor.ips.repository.TelebirrTransactionsRepo;
import com.transaction_failure_monitor.ips.service.handlerService.DailyTransactionHandlerService;
import com.transaction_failure_monitor.ips.util.SummaryBuilder;
import com.transaction_failure_monitor.ips.util.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TelebirrDailyAlertHandlerService implements DailyTransactionHandlerService<TelebirrTransactions> {
    private final TelebirrTransactionsRepo repo;

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.TELEBIRR;
    }

    @Override
    public List<TelebirrTransactions> getTodayTransactions() {
        return repo.findByTodayDate(String.valueOf(LocalDate.now()));
    }

    @Override
    public TransactionSummaryDTO buildSummary(List<?> transactions) {
        return SummaryBuilder.buildTelebirrTransactionSummary((List<TelebirrTransactions>) transactions);
    }
}
