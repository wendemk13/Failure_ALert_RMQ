package com.transaction_failure_monitor.ips.service;

import com.transaction_failure_monitor.ips.util.AlertType;

public interface DailyAggregateAlertService {
    String checkDailyAlert(AlertType alertType);

}
