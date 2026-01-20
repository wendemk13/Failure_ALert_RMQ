package com.transaction_failure_monitor.ips.service;

import com.transaction_failure_monitor.ips.util.AlertType;
import com.transaction_failure_monitor.ips.util.TransactionType;

public interface DailyAlertByTypeService {
    String checkDailyAlert(TransactionType type, AlertType alertType);
}
