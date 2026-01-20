package com.transaction_failure_monitor.ips.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(name = "TransactionSummaryDTO", description = "Summary statistics of transactions failures and amounts")
public class TransactionSummaryDTO {
    private int totalTransactions;
    private long failedTransactions;
    private double failureRate;
    private double totalAmount;
    private double failedAmount;
}
