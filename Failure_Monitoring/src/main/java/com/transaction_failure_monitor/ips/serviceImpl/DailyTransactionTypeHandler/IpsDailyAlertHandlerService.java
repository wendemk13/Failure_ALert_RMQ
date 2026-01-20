package com.transaction_failure_monitor.ips.serviceImpl.DailyTransactionTypeHandler;

import com.transaction_failure_monitor.ips.dto.TransactionSummaryDTO;
import com.transaction_failure_monitor.ips.entity.IpsTransactions;
import com.transaction_failure_monitor.ips.repository.IpsTransactionsRepo;
import com.transaction_failure_monitor.ips.service.handlerService.DailyTransactionHandlerService;
import com.transaction_failure_monitor.ips.util.SummaryBuilder;
import com.transaction_failure_monitor.ips.util.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IpsDailyAlertHandlerService implements DailyTransactionHandlerService<IpsTransactions> {

    private final IpsTransactionsRepo repo;

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.IPS;
    }

    @Override
    public List<IpsTransactions> getTodayTransactions() {
        return repo.findByTodayDate(String.valueOf(LocalDate.now()));
    }

    @Override
    public TransactionSummaryDTO buildSummary(List<?> transactions) {
        return SummaryBuilder.buildIpsTransactionSummary((List<IpsTransactions>) transactions);
    }
}
