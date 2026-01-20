package com.transaction_failure_monitor.ips.config;

import com.transaction_failure_monitor.ips.service.SMSAlertingService;
import com.transaction_failure_monitor.ips.serviceImpl.DailyAggregateAlertServiceImpl;
import com.transaction_failure_monitor.ips.util.AlertType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduledAlerting {

    private final Logger logger = LoggerFactory.getLogger(ScheduledAlerting.class);
    private final DailyAggregateAlertServiceImpl dailySummaryAlertingService;
    private final SMSAlertingService SMSAlertingService;


    @Scheduled(cron = "${ScheduledAlertingTime}")
    public void scheduledAlert() {
        try {
            logger.info("Scheduled alert started at {}", java.time.LocalDateTime.now());
            AlertType alertType = AlertType.SCHEDULED_ALERT;
            String response = dailySummaryAlertingService.checkDailyAlert(alertType);
            logger.info("Scheduled alert executed at {}", java.time.LocalDateTime.now());
            logger.info(response + "\n" + java.time.LocalDateTime.now());
        } catch (Exception e) {
            logger.error("Scheduled alert failed: {}", e.getMessage(), e);
            throw new RuntimeException("Scheduled alert failed: " + e.getMessage(), e);
        }

    }


}
