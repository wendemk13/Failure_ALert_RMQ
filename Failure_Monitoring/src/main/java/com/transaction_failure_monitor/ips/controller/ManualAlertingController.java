package com.transaction_failure_monitor.ips.controller;

import com.transaction_failure_monitor.ips.service.DailyAlertByTypeService;
import com.transaction_failure_monitor.ips.serviceImpl.DailyAggregateAlertServiceImpl;
import com.transaction_failure_monitor.ips.util.AlertType;
import com.transaction_failure_monitor.ips.util.TransactionType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;


@RestController
@RequestMapping("/api/v1/alert")
@AllArgsConstructor
@Tag(name = "Alerting", description = "Endpoint to send daily transaction alert.")
public class ManualAlertingController {
    private final DailyAggregateAlertServiceImpl dailyAlertingService;
    private final DailyAlertByTypeService dailyAlertByTypeService;


    //  Manual Aggregate Daily TransactionAlerts
    @Operation(summary = "Send Aggregate manual daily SMS alert",
            description = "Triggers a manual alert for all types of daily transaction failures")
    @GetMapping("/sms")
    public ResponseEntity<String> sendDailySmsAlert() {
        AlertType alertType = AlertType.MANUAL_ALERT;
        String result = dailyAlertingService.checkDailyAlert(alertType);
        return ResponseEntity.ok(result);
    }

    //  Manual Daily TransactionAlerts BY transaction type
    @Operation(summary = "Send manual daily SMS alert for the provided Transaction type",
            description = "Send a manual alert for the provided Transaction type daily failures")
    @GetMapping("/sms/{type}")
    public ResponseEntity<String> sendDailySmsAlertByType(@PathVariable String type) {
        TransactionType transactionType = null;
        try {
            transactionType = TransactionType.valueOf(type.toUpperCase());
            AlertType alertType = AlertType.MANUAL_ALERT;
            String result = dailyAlertByTypeService.checkDailyAlert(transactionType, alertType);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body("Invalid transaction type: " + type + ". Supported types: " + Arrays.toString(TransactionType.values()));
        }
    }

}
