package com.transaction_failure_monitor.ips.service;

import org.springframework.transaction.annotation.Transactional;

public interface TransactionPublisherService {
    @Transactional
    void publishPendingAlerts();
}
