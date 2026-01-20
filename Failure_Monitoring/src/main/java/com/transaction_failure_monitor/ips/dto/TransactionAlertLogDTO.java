package com.transaction_failure_monitor.ips.dto;

import com.transaction_failure_monitor.ips.util.AlertType;
import com.transaction_failure_monitor.ips.util.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "TransactionAlertLogDTO",
        description = "Data transfer object for transaction alert logs.")
public class TransactionAlertLogDTO {
    private long alertId;
    private String transactionId;
    private LocalDateTime alertTime;
    private AlertType alertType;
    private TransactionType transactionType;
    private double amount;
    private String message;
}
