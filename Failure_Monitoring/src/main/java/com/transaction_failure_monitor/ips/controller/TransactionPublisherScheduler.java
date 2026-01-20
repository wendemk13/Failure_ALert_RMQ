package com.transaction_failure_monitor.ips.controller;

import com.transaction_failure_monitor.ips.service.TransactionPublisherService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableScheduling
public class TransactionPublisherScheduler {

    private final TransactionPublisherService transactionPublisherService;

    @Scheduled(fixedDelayString = "200")
    public void run() {
        transactionPublisherService.publishPendingAlerts();
    }
}
