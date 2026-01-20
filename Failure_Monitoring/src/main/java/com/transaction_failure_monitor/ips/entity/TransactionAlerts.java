package com.transaction_failure_monitor.ips.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "alerts_table")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionAlerts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer alertId;
    private String transactionId;
    private String transactionType;
    private String amount;
    private String status;
    private LocalDateTime createdAt;
}
