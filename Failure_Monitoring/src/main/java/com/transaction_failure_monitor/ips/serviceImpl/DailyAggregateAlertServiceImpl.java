package com.transaction_failure_monitor.ips.serviceImpl;


import com.transaction_failure_monitor.ips.dto.TransactionSummaryDTO;
import com.transaction_failure_monitor.ips.entity.TransactionAlertLogs;
import com.transaction_failure_monitor.ips.repository.TransactionAlertLogsRepository;
import com.transaction_failure_monitor.ips.service.DailyAggregateAlertService;
import com.transaction_failure_monitor.ips.service.SMSAlertingService;
import com.transaction_failure_monitor.ips.service.handlerService.DailyTransactionHandlerService;
import com.transaction_failure_monitor.ips.serviceImpl.HandlerServiceRegistry.DailyTransactionHandlerRegistry;
import com.transaction_failure_monitor.ips.util.AlertType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DailyAggregateAlertServiceImpl implements DailyAggregateAlertService {
    private final DailyTransactionHandlerRegistry registry;
    private final TransactionAlertLogsRepository alertLogRepository;
    private final SMSAlertingService SMSAlertingService;


    @Value("${failure.rate.threshold}")
    private double FAILURE_RATE_THRESHOLD;


    @Override
    public String checkDailyAlert(AlertType alertType) {
        StringBuilder message = new StringBuilder();

        Collection<DailyTransactionHandlerService<?>> handlers = registry.getAllHandlers();

        if (handlers == null || handlers.isEmpty()) {
            message.append("No transaction handlers available. Cannot generate daily alert.\n");
        } else {
            for (DailyTransactionHandlerService<?> handler : handlers) {
                List<?> txs = handler.getTodayTransactions();
                TransactionSummaryDTO summary = handler.buildSummary(txs);

                boolean thresholdExceeded = summary.getFailureRate() > FAILURE_RATE_THRESHOLD;

                message.append("Transaction Type: ").append(handler.getTransactionType()).append("\n")
                        .append("Total Transactions: ").append(summary.getTotalTransactions()).append("\n")
                        .append("Failed Transactions: ").append(summary.getFailedTransactions())
                        .append(" (").append(String.format("%.1f", summary.getFailureRate())).append("%)").append("\n")
                        .append("Amount Impacted: ").append(String.format("%,.0f", summary.getFailedAmount())).append("\n")
                        .append("Status: ").append(thresholdExceeded ? "Threshold Exceeded" : "Running Normally")
                        .append("\n\n");
            }
        }

        String finalMessage = message.toString();

        TransactionAlertLogs log = new TransactionAlertLogs();
        log.setAlertTime(LocalDateTime.now());
        log.setAlertType(alertType);
        log.setMessage(finalMessage);
        alertLogRepository.save(log);
        SMSAlertingService.sendSMS(finalMessage);

        return finalMessage;
    }


}

