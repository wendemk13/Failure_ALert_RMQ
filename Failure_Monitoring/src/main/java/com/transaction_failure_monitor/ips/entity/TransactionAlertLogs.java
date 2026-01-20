package com.transaction_failure_monitor.ips.entity;

import com.transaction_failure_monitor.ips.util.AlertType;
import com.transaction_failure_monitor.ips.util.TransactionType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transactionalertlog")
@Schema(name = "TransactionAlertLogs", description = "Entity representing a transaction alert log")
public class TransactionAlertLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alert_id")
    private Long alertId;

    @Column(name = "alert_time", columnDefinition = "datetime(6)")
    private LocalDateTime alertTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "alert_type", nullable = false, columnDefinition = "ENUM('IMMEDIATE_ALERT','SCHEDULED_ALERT','MANUAL_ALERT')")
    private AlertType alertType;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column(name = "transaction_amount")
    private Double amount;

    @Column(name = "message", nullable = false, length = 255)
    private String message;

    @Column(name = "percentage_threshold_exceeded", nullable = false)
    private Boolean percentageThresholdExceeded;

    @Column(name = "amount_threshold_exceeded", nullable = false)
    private Boolean amountThresholdExceeded;

    @Column(name = "failure_percent_during_alert", nullable = false)
    private Double failurePercentDuringAlert;

    @Column(name = "total_amount_during_alert", nullable = false)
    private Double totalAmountDuringAlert;

    @Column(name = "transaction_id", length = 100)
    private String transactionId;

    @Column(name = "amount_threshold_exceeded_status", insertable = false, updatable = false)
    private String amountThresholdExceededStatus;

    @Column(name = "percentage_threshold_exceeded_status", insertable = false, updatable = false)
    private String percentageThresholdExceededStatus;


}
