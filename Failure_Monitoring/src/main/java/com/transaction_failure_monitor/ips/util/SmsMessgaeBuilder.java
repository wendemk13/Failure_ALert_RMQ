package com.transaction_failure_monitor.ips.util;


import com.transaction_failure_monitor.ips.dto.TransactionSummaryDTO;

public class SmsMessgaeBuilder {

    public static String buildDailyTypeAlertMessage(AlertType type, TransactionType transactionType, TransactionSummaryDTO summary) {
        return String.format(
                "MANUAL %s DAILY SUMMARY\n" +
                        "Failed transactions: %d/%d (%.1f%%)\n" +
                        "Amount impacted: %,.0f Birr",
                transactionType,
                summary.getFailedTransactions(),
                summary.getTotalTransactions(),
                summary.getFailureRate(),
                summary.getFailedAmount()
        );
    }

    public static String buildImmediateAlertMessage(TransactionSummaryDTO summary, String transactionId, TransactionType transactionType) {
        return String.format(
                "Immediate TransactionAlerts\n" +
                        "New Failed %s Transaction added. Transaction ID: %s\n" +
                        "Total failed: (%d/%d) transactions (%.1f%%)\n" +
                        "Total failed amount reached : %.2f Birr.",
                transactionType,
                transactionId,
                summary.getFailedTransactions(),
                summary.getTotalTransactions(),
                summary.getFailureRate(),
                summary.getFailedAmount()
        );
    }


}
