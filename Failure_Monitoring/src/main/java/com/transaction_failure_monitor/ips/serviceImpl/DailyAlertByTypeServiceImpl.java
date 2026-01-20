package com.transaction_failure_monitor.ips.serviceImpl;

import com.transaction_failure_monitor.ips.dto.TransactionSummaryDTO;
import com.transaction_failure_monitor.ips.entity.TransactionAlertLogs;
import com.transaction_failure_monitor.ips.exception.SMSAlertingServiceException;
import com.transaction_failure_monitor.ips.exception.TransactionHandlerNotFound;
import com.transaction_failure_monitor.ips.repository.TransactionAlertLogsRepository;
import com.transaction_failure_monitor.ips.service.DailyAlertByTypeService;
import com.transaction_failure_monitor.ips.service.SMSAlertingService;
import com.transaction_failure_monitor.ips.service.handlerService.DailyTransactionHandlerService;
import com.transaction_failure_monitor.ips.serviceImpl.HandlerServiceRegistry.DailyTransactionHandlerRegistry;
import com.transaction_failure_monitor.ips.util.AlertType;
import com.transaction_failure_monitor.ips.util.SmsMessgaeBuilder;
import com.transaction_failure_monitor.ips.util.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DailyAlertByTypeServiceImpl implements DailyAlertByTypeService {
    private final TransactionAlertLogsRepository alertLogRepository;
    private final SMSAlertingService smsAlerting;
    private final DailyTransactionHandlerRegistry dailyRegistry;
    @Value("${failure.rate.threshold}")
    private double FAILURE_RATE_THRESHOLD;


    @Override
    public String checkDailyAlert(TransactionType transactionType, AlertType alertType) {
        DailyTransactionHandlerService<?> handler = dailyRegistry.getHandler(transactionType);
        if (handler == null) {
            throw new TransactionHandlerNotFound("No handler registered for transaction type: " + transactionType);
        }

        List<?> todayTransactions = handler.getTodayTransactions();

        TransactionSummaryDTO summary = handler.buildSummary(todayTransactions);

        boolean failurePercentExceeded = summary.getFailureRate() > FAILURE_RATE_THRESHOLD;

        String alertMessage;
        if (!failurePercentExceeded) {
            alertMessage = String.format(
                    "Today %s transactions are running successfully according to the threshold.\n" +
                            "Failed transactions: %d/%d (%.1f%%)\n" +
                            "Amount impacted: %,.0f\n",
                    transactionType,
                    summary.getFailedTransactions(),
                    summary.getTotalTransactions(),
                    summary.getFailureRate(),
                    summary.getFailedAmount()
            );
        } else {
            alertMessage = SmsMessgaeBuilder.buildDailyTypeAlertMessage(alertType, transactionType, summary);
        }

        TransactionAlertLogs log = new TransactionAlertLogs();
        log.setAlertTime(LocalDateTime.now());
        log.setAlertType(alertType);
        log.setFailurePercentDuringAlert(summary.getFailureRate());
        log.setPercentageThresholdExceeded(failurePercentExceeded);
        log.setTotalAmountDuringAlert(summary.getFailedAmount());
        log.setMessage(alertMessage);
        log.setTransactionType(transactionType);

        alertLogRepository.save(log);

        try {
            smsAlerting.sendSMS(alertMessage);
        } catch (Exception e) {
            throw new SMSAlertingServiceException("Failed to send alert SMS: " + e.getMessage());
        }
        return alertMessage;
    }

}


