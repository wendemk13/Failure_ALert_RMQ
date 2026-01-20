package com.transaction_failure_monitor.ips.serviceImpl;

import com.transaction_failure_monitor.ips.config.RMQConfig;
import com.transaction_failure_monitor.ips.entity.TransactionAlerts;
import com.transaction_failure_monitor.ips.repository.AlertsRepo;
import com.transaction_failure_monitor.ips.service.TransactionPublisherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionTransactionPublisherServiceImpl implements TransactionPublisherService {
    private final AlertsRepo alertsRepo;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    @Override
    public void publishPendingAlerts() {
        List<TransactionAlerts> events = alertsRepo.findByStatus("PENDING");

        for (TransactionAlerts event : events) {
            try {
                rabbitTemplate.convertAndSend(
                        RMQConfig.TRANSACTION_ALERT_EXCHANGE,
                        RMQConfig.TRANSACTION_QUEUE,
                        event
                );
                event.setStatus("SENT");
            } catch (Exception e) {
                log.error("Failed to publish event {}", event.getAlertId(), e);
            }
        }
    }

}

