package com.transaction_failure_monitor.ips.util;

import com.transaction_failure_monitor.ips.dto.AlertLogSummaryDTO;
import com.transaction_failure_monitor.ips.dto.TransactionSummaryDTO;
import com.transaction_failure_monitor.ips.entity.IpsTransactions;
import com.transaction_failure_monitor.ips.entity.TelebirrTransactions;
import com.transaction_failure_monitor.ips.entity.TransactionAlertLogs;
import com.transaction_failure_monitor.ips.util.transactionStatus.IpsTransactionStatus;
import com.transaction_failure_monitor.ips.util.transactionStatus.TelebirrTransactionStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

public class SummaryBuilder {

    public static TransactionSummaryDTO buildIpsTransactionSummary(List<IpsTransactions> txns) {
        int total = txns.size();
        long failed = txns.stream()
                .filter(t -> IpsTransactionStatus.FAILED.name().equalsIgnoreCase(t.getTransactionStatus()))
                .count();

        double totalAmount = txns.stream()
                .mapToDouble(t -> Double.parseDouble(t.getAmount()))
                .sum();

        double failedAmount = txns.stream()
                .filter(t -> IpsTransactionStatus.FAILED.name()
                        .equalsIgnoreCase(t.getTransactionStatus()))
                .mapToDouble(t -> Double.parseDouble(t.getAmount()))
                .sum();
        double failureRate = roundPercentageToDecimals(total > 0 ? (failed * 100.0) / total : 0);
        return new TransactionSummaryDTO(total, failed, failureRate, totalAmount, failedAmount);
    }

    public static TransactionSummaryDTO buildTelebirrTransactionSummary(List<TelebirrTransactions> txns) {
        int total = txns.size();
        long failed = txns.stream()
                .filter(t -> TelebirrTransactionStatus.NO.name().equalsIgnoreCase(t.getStatusTransfer()))
                .count();

        double totalAmount = txns.stream()
                .mapToDouble(t -> t.getAmount())
                .sum();

        double failedAmount = txns.stream()
                .filter(t -> TelebirrTransactionStatus.NO.name().equalsIgnoreCase(t.getStatusTransfer()))
                .mapToDouble(t -> t.getAmount())
                .sum();
        double failureRate = roundPercentageToDecimals(total > 0 ? (failed * 100.0) / total : 0);
        return new TransactionSummaryDTO(total, failed, failureRate, totalAmount, failedAmount);
    }


    public static AlertLogSummaryDTO buildAlertLogsSummary(List<TransactionAlertLogs> logs) {

        long total = logs.size();

        // Count by AlertType
        Map<AlertType, Long> alertTypeCounts = Arrays.stream(AlertType.values())
                .collect(Collectors.toMap(
                        alertType -> alertType,
                        alertType -> logs.stream().filter(l -> l.getAlertType() == alertType).count()
                ));

        // Count by TransactionType
        Map<TransactionType, Long> transactionTypeCounts = Arrays.stream(TransactionType.values())
                .collect(Collectors.toMap(
                        transactionType -> transactionType,
                        transactionType -> logs.stream().filter(l -> l.getTransactionType() == transactionType).count()
                ));

        // Total amount
        double totalAmount = logs.stream()
                .mapToDouble(l -> Optional.ofNullable(l.getAmount()).orElse(0.0))
                .sum();

        // Total amount by transaction type
        Map<TransactionType, Double> transactionTypeAmounts = Arrays.stream(TransactionType.values())
                .collect(Collectors.toMap(
                        tt -> tt,
                        tt -> logs.stream()
                                .filter(l -> l.getTransactionType() == tt)
                                .mapToDouble(l -> Optional.ofNullable(l.getAmount()).orElse(0.0))
                                .sum()
                ));

        // Latest failure rate by transaction type
        Map<TransactionType, Double> latestFailureRates = Arrays.stream(TransactionType.values())
                .collect(Collectors.toMap(
                        tt -> tt,
                        tt -> logs.stream()
                                .filter(l -> l.getTransactionType() == tt)
                                .max(Comparator.comparing(TransactionAlertLogs::getAlertTime))
                                .map(TransactionAlertLogs::getFailurePercentDuringAlert)
                                .orElse(0.0)
                ));

        return new AlertLogSummaryDTO(
                total,
                alertTypeCounts.get(AlertType.IMMEDIATE_ALERT),
                alertTypeCounts.get(AlertType.SCHEDULED_ALERT),
                alertTypeCounts.get(AlertType.MANUAL_ALERT),
                transactionTypeCounts.get(TransactionType.IPS),
                transactionTypeCounts.get(TransactionType.TELEBIRR),
                transactionTypeCounts.get(TransactionType.AIRTIME),
                latestFailureRates.get(TransactionType.IPS),
                latestFailureRates.get(TransactionType.TELEBIRR),
                latestFailureRates.get(TransactionType.AIRTIME),
                totalAmount,
                transactionTypeAmounts.get(TransactionType.IPS),
                transactionTypeAmounts.get(TransactionType.TELEBIRR),
                transactionTypeAmounts.get(TransactionType.AIRTIME)
        );
    }


    private static double roundPercentageToDecimals(double value) {
        return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }

}
