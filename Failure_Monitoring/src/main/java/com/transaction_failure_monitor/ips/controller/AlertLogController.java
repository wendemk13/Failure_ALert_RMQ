package com.transaction_failure_monitor.ips.controller;

import com.transaction_failure_monitor.ips.dto.AlertLogSummaryDTO;
import com.transaction_failure_monitor.ips.dto.TransactionAlertLogDTO;
import com.transaction_failure_monitor.ips.exception.InvalidArgumentException;
import com.transaction_failure_monitor.ips.service.TransactionAlertLogService;
import com.transaction_failure_monitor.ips.util.AlertType;
import com.transaction_failure_monitor.ips.util.TransactionType;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
@Tag(name = "TransactionAlerts Logs", description = "Endpoints related to transaction alert logs")
public class AlertLogController {
    private final TransactionAlertLogService transactionAlertLogService;


    //GET All TransactionAlerts Logs
    @GetMapping("/alertlogs")
    public ResponseEntity<Page<TransactionAlertLogDTO>> getTransactionAlertLogs(
            @PageableDefault(size = 20, sort = "alertTime", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) String transactionType,
            @RequestParam(required = false) String alertType) {

        TransactionType txType = null;
        if (transactionType != null && !transactionType.isBlank()) {
            try {
                txType = TransactionType.valueOf(transactionType.toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new InvalidArgumentException(
                        "Invalid transaction type. Allowed values: " + Arrays.toString(TransactionType.values())
                );
            }
        }

        AlertType altType = null;
        if (alertType != null && !alertType.isBlank()) {
            try {
                altType = AlertType.valueOf(alertType.toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new InvalidArgumentException(
                        "Invalid alert type. Allowed values: " + Arrays.toString(AlertType.values())
                );
            }
        }
        Page<TransactionAlertLogDTO> alertLogsPage =
                transactionAlertLogService.getAllTransactionAlertLogs(txType, altType, pageable);
        int maxPage = Math.max(alertLogsPage.getTotalPages() - 1, 0);
        if (pageable.getPageNumber() > maxPage) {
            throw new InvalidArgumentException(
                    String.format("Invalid page number %d. Total pages available: 0 to %d", pageable.getPageNumber(), maxPage)
            );
        }
        return ResponseEntity.ok(alertLogsPage);
    }


    //GET Daily TransactionAlerts Logs
    @GetMapping("/alertlogs/daily")
    public ResponseEntity<Page<TransactionAlertLogDTO>> getTransactionLogs(
            @RequestParam(required = false) String transactionType,
            @RequestParam(required = false) String alertType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 20, sort = "alertTime", direction = Sort.Direction.DESC) Pageable pageable) {
        TransactionType txType = null;
        if (transactionType != null && !transactionType.isBlank()) {
            try {
                txType = TransactionType.valueOf(transactionType.toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new InvalidArgumentException(
                        "Invalid alert type. Allowed values: " + Arrays.toString(TransactionType.values())
                );
            }
        }

        AlertType altType = null;
        if (alertType != null && !alertType.isBlank()) {
            try {
                altType = AlertType.valueOf(alertType.toUpperCase());
            } catch (IllegalArgumentException ex) {
                throw new InvalidArgumentException(
                        "Invalid alert type. Allowed values: " + Arrays.toString(AlertType.values())
                );
            }
        }
        if (startDate == null) startDate = LocalDate.now();
        if (endDate == null) endDate = startDate;

        if (startDate.isAfter(endDate)) {
            throw new InvalidArgumentException("Invalid start date. Start date must be before end date.");
        }
        if (startDate.isAfter(LocalDate.now()) || endDate.isAfter(LocalDate.now())) {
            throw new InvalidArgumentException("Invalid date,Date can not be in the future.");
        }

        Page<TransactionAlertLogDTO> page =
                transactionAlertLogService.getLogsBetweenDates(txType, altType, startDate, endDate, pageable);

        int maxPage = Math.max(page.getTotalPages() - 1, 0);
        if (pageable.getPageNumber() > maxPage) {
            throw new InvalidArgumentException(
                    String.format("Invalid page number %d. Total pages available: 0 to %d", pageable.getPageNumber(), maxPage)
            );
        }

        return ResponseEntity.ok(page);
    }

    //GET TransactionAlerts Log By ID
    @GetMapping("/alertlogs/{id}")
    public ResponseEntity<TransactionAlertLogDTO> getAlertById(@PathVariable long id) {
        TransactionAlertLogDTO alert;
        try {
            alert = transactionAlertLogService.getAlertById(id);
        } catch (NumberFormatException e) {
            throw new InvalidArgumentException("Invalid ID format, must be numeric");
        }
        return ResponseEntity.ok(alert);
    }

    //Get summary
    @GetMapping("/alertlogs/summary")
    public ResponseEntity<AlertLogSummaryDTO> getAlertSummary(
            @RequestParam(required = false)
            @Parameter(description = "Start date for summary. Defaults to today if not provided")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false)
            @Parameter(description = "End date for summary. Defaults to startDate if only start date provided")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(required = false, name = "days")
            @Parameter(description = "Optional: last N days including today. Overrides startDate and endDate if provided")
            Integer days) {
        AlertLogSummaryDTO response;
        if (days != null) {
            response = transactionAlertLogService.getAlertSummaryForNDays(days);
        } else {
            response = transactionAlertLogService.getAlertSummary(startDate, endDate);
        }
        return ResponseEntity.ok(response);
    }

}
