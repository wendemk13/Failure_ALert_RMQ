package com.transaction_failure_monitor.ips.serviceImpl.ImmediateAlerthandlers;

import com.transaction_failure_monitor.ips.dto.TransactionSummaryDTO;
import com.transaction_failure_monitor.ips.entity.IpsTransactions;
import com.transaction_failure_monitor.ips.exception.TransactionNotFoundException;
import com.transaction_failure_monitor.ips.repository.IpsTransactionsRepo;
import com.transaction_failure_monitor.ips.service.handlerService.ImmediateTransactionHandlerService;
import com.transaction_failure_monitor.ips.util.SummaryBuilder;
import com.transaction_failure_monitor.ips.util.TransactionType;
import com.transaction_failure_monitor.ips.util.transactionStatus.IpsTransactionStatus;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IPSImmediateAlertHandler implements ImmediateTransactionHandlerService<IpsTransactions> {

    private final IpsTransactionsRepo ipsTransactionsRepo;
    private final Logger logger = LoggerFactory.getLogger(IPSImmediateAlertHandler.class);

    @Override
    public TransactionType getTransactionType() {
        return TransactionType.IPS;
    }

    @Override
    public IpsTransactions findTransaction(String transactionId) throws TransactionNotFoundException {
        return ipsTransactionsRepo.findByTransid(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("IPS Transaction not found: " + transactionId));
    }

    @Override
    public boolean isTransactionFailed(IpsTransactions transaction) {
        IpsTransactions tx = transaction;
        return IpsTransactionStatus.FAILED.name()
                .equalsIgnoreCase(tx.getTransactionStatus());
    }

    @Override
    public List<IpsTransactions> getTodayTransactions() {
        return ipsTransactionsRepo.findByTodayDate(String.valueOf(LocalDate.now()));
    }

    @Override
    public TransactionSummaryDTO buildTransactionSummary(List<IpsTransactions> transactions) {
        return SummaryBuilder.buildIpsTransactionSummary(transactions);
    }

    // success message
    @Override
    public String buildSuccessMessage(TransactionSummaryDTO summary) {
        return String.format(
                "Today IPS IpsTransactions are running successfully.\n" +
                        "Failed transactions: %d/%d (%.1f%%)\n" +
                        "Amount impacted: %,.0f\n",
                summary.getFailedTransactions(),
                summary.getTotalTransactions(),
                summary.getFailureRate(),
                summary.getFailedAmount()
        );
    }
}