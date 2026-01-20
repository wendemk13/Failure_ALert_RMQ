package com.transaction_failure_monitor.ips.serviceImpl;

import com.rabbitmq.client.Channel;
import com.transaction_failure_monitor.ips.config.RMQConfig;
import com.transaction_failure_monitor.ips.dto.TransactionSummaryDTO;
import com.transaction_failure_monitor.ips.entity.TransactionAlerts;
import com.transaction_failure_monitor.ips.entity.TransactionAlertLogs;
import com.transaction_failure_monitor.ips.repository.AlertsRepo;
import com.transaction_failure_monitor.ips.repository.TransactionAlertLogsRepository;
import com.transaction_failure_monitor.ips.service.ImmediateAlertListenerService;
import com.transaction_failure_monitor.ips.service.SMSAlertingService;
import com.transaction_failure_monitor.ips.service.handlerService.ImmediateTransactionHandlerService;
import com.transaction_failure_monitor.ips.serviceImpl.HandlerServiceRegistry.ImmediateTransactionHandlerRegistry;
import com.transaction_failure_monitor.ips.util.AlertType;
import com.transaction_failure_monitor.ips.util.SmsMessgaeBuilder;
import com.transaction_failure_monitor.ips.util.TransactionType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImmediateAlertListenerServiceImpl implements ImmediateAlertListenerService {
    private final AlertsRepo alertsRepo;
    private final ImmediateTransactionHandlerRegistry handlerRegistry;
    private final TransactionAlertLogsRepository alertLogRepository;
    private final SMSAlertingService SMSAlertingService;

    @Value("${failure.rate.threshold}")
    private double FAILURE_RATE_THRESHOLD;

    @RabbitListener(queues = RMQConfig.TRANSACTION_QUEUE, ackMode = "MANUAL")
    @Override
    @Transactional
    public void handleImmediateAlertMessage(TransactionAlerts message, Channel channel,
                                            @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        try {
            if (isAlertAlreadyProcessed(message.getTransactionId())) {
                log.info("TransactionAlerts already processed for transaction {}", message.getTransactionId());
                channel.basicAck(tag, false);
                return;
            }
            // Get handler
            ImmediateTransactionHandlerService handler = handlerRegistry.getHandler(message.getTransactionType());
            if (handler == null) {
                log.warn("No handler registered for transaction type: {}", message.getTransactionType());
                channel.basicAck(tag, false);
                return;
            }
            // Find transaction
            Object transaction = handler.findTransaction(message.getTransactionId());
            if (transaction == null || !handler.isTransactionFailed(transaction)) {
                log.info("Transaction {} not found or not failed", message.getTransactionId());
                channel.basicAck(tag, false);
                return;
            }
            // Build summary and SMS message
            List<?> todayTransactions = handler.getTodayTransactions();
            TransactionSummaryDTO summary = handler.buildTransactionSummary(todayTransactions);
            String alertMessage;
            if (!isThresholdExceeded(summary)) {
                alertMessage = handler.buildSuccessMessage(summary);
            } else {
                alertMessage = SmsMessgaeBuilder.buildImmediateAlertMessage(summary, message.getTransactionId(), TransactionType.valueOf(message.getTransactionType()));
            }
            SMSAlertingService.sendSMS(alertMessage);
            // Save alert log
            saveAlertLog(message, summary, alertMessage);
            // Mark alert as SENT in DB and Ack
            message.setStatus("SENT");
            alertsRepo.save(message);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("Failed to process immediate alert for transaction {}", message.getTransactionId(), e);
            //requeue to DLQ
            channel.basicNack(tag, false, false);
        }
    }

    // helper methods
    private boolean isAlertAlreadyProcessed(String transactionId) {
        return alertLogRepository.existsByTransactionIdAndAlertType(
                transactionId, AlertType.IMMEDIATE_ALERT);
    }

    private boolean isThresholdExceeded(TransactionSummaryDTO summary) {
        return summary.getFailureRate() >= FAILURE_RATE_THRESHOLD;
    }

    private void saveAlertLog(TransactionAlerts message, TransactionSummaryDTO summary, String smsMessage) {
        TransactionAlertLogs log = new TransactionAlertLogs();
        log.setTransactionId(message.getTransactionId());
        log.setTransactionType(TransactionType.valueOf(message.getTransactionType()));
        log.setAlertType(AlertType.IMMEDIATE_ALERT);
        log.setAlertTime(LocalDateTime.now());
        log.setFailurePercentDuringAlert(summary.getFailureRate());
        log.setPercentageThresholdExceeded(summary.getFailureRate() >= FAILURE_RATE_THRESHOLD);
        log.setTotalAmountDuringAlert(summary.getFailedAmount());
        log.setMessage(smsMessage);
        log.setAmount(Double.valueOf(message.getAmount()));
        alertLogRepository.save(log);
    }
}






