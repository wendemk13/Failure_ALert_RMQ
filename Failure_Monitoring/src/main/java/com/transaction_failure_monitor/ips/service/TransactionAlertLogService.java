package com.transaction_failure_monitor.ips.service;

import com.transaction_failure_monitor.ips.dto.AlertLogSummaryDTO;
import com.transaction_failure_monitor.ips.dto.TransactionAlertLogDTO;
import com.transaction_failure_monitor.ips.util.AlertType;
import com.transaction_failure_monitor.ips.util.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface TransactionAlertLogService {

    Page<TransactionAlertLogDTO> getAllTransactionAlertLogs(TransactionType type, AlertType alertType, Pageable pageable);


    TransactionAlertLogDTO getAlertById(long id);

    Page<TransactionAlertLogDTO> getLogsBetweenDates(TransactionType transactionType,
                                                     AlertType alertType,
                                                     LocalDate startDate,
                                                     LocalDate endDate,
                                                     Pageable pageable);

    AlertLogSummaryDTO getAlertSummary(LocalDate startDate, LocalDate endDate);

    AlertLogSummaryDTO getAlertSummaryForNDays(int nDays);

}
