package com.transaction_failure_monitor.ips.serviceImpl.ImmediateAlerthandlers;


import com.transaction_failure_monitor.ips.dto.TransactionSummaryDTO;
import com.transaction_failure_monitor.ips.entity.TelebirrTransactions;
import com.transaction_failure_monitor.ips.exception.TransactionNotFoundException;
import com.transaction_failure_monitor.ips.repository.TelebirrTransactionsRepo;
import com.transaction_failure_monitor.ips.service.handlerService.ImmediateTransactionHandlerService;
import com.transaction_failure_monitor.ips.util.SummaryBuilder;
import com.transaction_failure_monitor.ips.util.TransactionType;
import com.transaction_failure_monitor.ips.util.transactionStatus.TelebirrTransactionStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TelebirrImmediateAlertHandler implements ImmediateTransactionHandlerService<TelebirrTransactions> {

    private final TelebirrTransactionsRepo telebirrTransactionsRepo;


    @Override
    public TransactionType getTransactionType() {
        return TransactionType.TELEBIRR;
    }

    @Override
    public TelebirrTransactions findTransaction(String transactionId) throws TransactionNotFoundException {
        return telebirrTransactionsRepo.findById(Integer.valueOf(transactionId))
                .orElseThrow(() -> new TransactionNotFoundException(
                        "Telebirr Transaction not found: " + transactionId));
    }

    @Override
    public boolean isTransactionFailed(TelebirrTransactions transaction) {
        TelebirrTransactions tx = transaction;
        return !TelebirrTransactionStatus.YES.name()
                .equalsIgnoreCase(tx.getStatusTransfer());
    }

    @Override
    public List<TelebirrTransactions> getTodayTransactions() {
        return telebirrTransactionsRepo.findByTodayDate(String.valueOf(LocalDate.now()));
    }

    @Override
    public TransactionSummaryDTO buildTransactionSummary(List<TelebirrTransactions> transactions) {
        return SummaryBuilder.buildTelebirrTransactionSummary(transactions);
    }

    @Override
    public String buildSuccessMessage(TransactionSummaryDTO summary) {
        return String.format(
                "Today Telebirr IpsTransactions are running successfully.\n" +
                        "Failed transactions: %d/%d (%.1f%%)\n" +
                        "Amount impacted: %,.0f\n",
                summary.getFailedTransactions(),
                summary.getTotalTransactions(),
                summary.getFailureRate(),
                summary.getFailedAmount()
        );
    }
}