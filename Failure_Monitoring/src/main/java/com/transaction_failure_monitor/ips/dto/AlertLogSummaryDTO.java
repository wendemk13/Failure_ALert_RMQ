package com.transaction_failure_monitor.ips.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlertLogSummaryDTO {

    // Counts
    private long totalAlerts;

    //Counts by ALERT type
    private long immediateAlerts;
    private long scheduledAlerts;
    private long manualAlerts;

    //Counts by transaction type
    private long ipsFailures;
    private long telebirrFailures;
    private long airtimeFailures;

    // Failure rate By TransactionType
    private double IPSFailureRate;
    private double TelebirrFailureRate;
    private double AirTimeFailureRate;

    // Total Amount Failed
    private double totalFailedAmount;

    // Amount Failed by TransactionType
    private double totalFailedIPSAmount;
    private double totalFailedTelebirrAmount;
    private double totalFailedAirTimeAmount;
}

