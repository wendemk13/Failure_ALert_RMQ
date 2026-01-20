package com.transaction_failure_monitor.ips.serviceImpl;

import com.transaction_failure_monitor.ips.dto.AlertLogSummaryDTO;
import com.transaction_failure_monitor.ips.dto.TransactionAlertLogDTO;
import com.transaction_failure_monitor.ips.entity.TransactionAlertLogs;
import com.transaction_failure_monitor.ips.exception.AlertLogServiceException;
import com.transaction_failure_monitor.ips.exception.InvalidArgumentException;
import com.transaction_failure_monitor.ips.exception.TransactionNotFoundException;
import com.transaction_failure_monitor.ips.repository.TransactionAlertLogsRepository;
import com.transaction_failure_monitor.ips.service.TransactionAlertLogService;
import com.transaction_failure_monitor.ips.util.AlertType;
import com.transaction_failure_monitor.ips.util.SummaryBuilder;
import com.transaction_failure_monitor.ips.util.TransactionType;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


@Service
@AllArgsConstructor
public class TransactionAlertLogServiceImpl implements TransactionAlertLogService {

    private final TransactionAlertLogsRepository transactionAlertLogsRepository;

    @Override
    public Page<TransactionAlertLogDTO> getAllTransactionAlertLogs(TransactionType transactionType, AlertType alertType, Pageable pageable) {
        if (transactionType == null && alertType == null) {
            return transactionAlertLogsRepository.findAll(pageable).map(this::toTransactionAlertLogDto);
        } else if (transactionType != null && alertType != null) {
            return transactionAlertLogsRepository.findByTransactionTypeAndAlertType(transactionType, alertType, pageable).map(this::toTransactionAlertLogDto);
        } else if (transactionType != null) {
            return transactionAlertLogsRepository.findByTransactionType(transactionType, pageable).map(this::toTransactionAlertLogDto);
        } else {
            return transactionAlertLogsRepository.findByAlertType(alertType, pageable).map(this::toTransactionAlertLogDto);
        }
    }


    @Override
    public TransactionAlertLogDTO getAlertById(long id) {
        Optional<TransactionAlertLogs> alertLogOpt = transactionAlertLogsRepository.findById(id);

        if (alertLogOpt.isEmpty()) {
            throw new TransactionNotFoundException("TransactionAlerts Log not found with ID: " + id);
        }
        TransactionAlertLogs alertLog = alertLogOpt.get();
        TransactionAlertLogDTO dto = new TransactionAlertLogDTO();
        dto.setAlertId(alertLog.getAlertId());
        dto.setTransactionId(alertLog.getTransactionId());
        dto.setAlertTime(alertLog.getAlertTime());
        dto.setAlertType(alertLog.getAlertType());
        dto.setTransactionType(alertLog.getTransactionType());
        dto.setAmount(alertLog.getAmount() != null ? alertLog.getAmount() : 0.0);
        dto.setMessage(alertLog.getMessage());

        return dto;
    }


    @Override
    public Page<TransactionAlertLogDTO> getLogsBetweenDates(TransactionType transactionType,
                                                            AlertType alertType,
                                                            LocalDate startDate,
                                                            LocalDate endDate,
                                                            Pageable pageable) {
        LocalDateTime startAlertTime = startDate.atStartOfDay();
        LocalDateTime endAlertTime = endDate.atTime(LocalTime.MAX);
        Page<TransactionAlertLogs> alertLogPage;

        try {
            if (transactionType == null && alertType == null) {
                alertLogPage = transactionAlertLogsRepository
                        .findByAlertTimeBetween(startAlertTime, endAlertTime, pageable);
            } else if (transactionType != null && alertType != null) {
                alertLogPage = transactionAlertLogsRepository.findByAlertTimeBetweenAndTransactionTypeAndAlertType(startAlertTime, endAlertTime, transactionType, alertType, pageable);
            } else if (transactionType != null) {
                alertLogPage = transactionAlertLogsRepository
                        .findByAlertTimeBetweenAndTransactionType(startAlertTime, endAlertTime, transactionType, pageable);
            } else {
                alertLogPage = transactionAlertLogsRepository
                        .findByAlertTimeBetweenAndAlertType(startAlertTime, endAlertTime, alertType, pageable);
            }
            return alertLogPage.map(this::toTransactionAlertLogDto);
        } catch (Exception e) {
            throw new AlertLogServiceException("Failed to fetch daily alert logs", e);
        }
    }


    @Override
    public AlertLogSummaryDTO getAlertSummary(LocalDate startDate, LocalDate endDate) {
        if (startDate == null && endDate == null) {
            startDate = endDate = LocalDate.now();
        } else if (startDate == null) {
            startDate = endDate;
        } else if (endDate == null) {
            endDate = startDate;
        }
        if (startDate.isAfter(LocalDate.now())) {
            throw new InvalidArgumentException("Start date cannot be in the future.");
        }
        if (startDate.isAfter(endDate)) {
            throw new InvalidArgumentException("Start date cannot be after end date.");
        }

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        try {
            List<TransactionAlertLogs> logs = transactionAlertLogsRepository.findByAlertTimeBetween(startDateTime, endDateTime);
            AlertLogSummaryDTO summary = SummaryBuilder.buildAlertLogsSummary(logs);
            return summary;
        } catch (Exception e) {
            throw new AlertLogServiceException("Failed to fetch alert summary", e);
        }
    }

    @Override
    public AlertLogSummaryDTO getAlertSummaryForNDays(int days) {
        if (days <= 0) {
            throw new InvalidArgumentException("Days must be greater than 0.");
        }
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = LocalDate.now().minusDays(days - 1);

        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);

        try {
            List<TransactionAlertLogs> logs = transactionAlertLogsRepository.findByAlertTimeBetween(startDateTime, endDateTime);
            AlertLogSummaryDTO summary = SummaryBuilder.buildAlertLogsSummary(logs);
            return summary;
        } catch (Exception e) {
            throw new AlertLogServiceException("Failed to fetch alert summary", e);
        }
    }


    // convert TransactionAlertLogs to TransactionAlertLogDTO
    private TransactionAlertLogDTO toTransactionAlertLogDto(TransactionAlertLogs entity) {
        TransactionAlertLogDTO dto = new TransactionAlertLogDTO();
        dto.setAlertId(entity.getAlertId());
        dto.setTransactionId(entity.getTransactionId());
        dto.setAlertType(entity.getAlertType());
        dto.setAlertTime(entity.getAlertTime());
        dto.setMessage(entity.getMessage());
        dto.setTransactionType(entity.getTransactionType());
        dto.setAmount(entity.getAmount() != null ? entity.getAmount() : 0.0);
        return dto;
    }
}
