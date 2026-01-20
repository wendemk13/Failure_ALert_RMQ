package com.transaction_failure_monitor.ips.repository;

import com.transaction_failure_monitor.ips.entity.TransactionAlertLogs;
import com.transaction_failure_monitor.ips.util.AlertType;
import com.transaction_failure_monitor.ips.util.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionAlertLogsRepository extends JpaRepository<TransactionAlertLogs, Long> {


    boolean existsByTransactionIdAndAlertType(String transactionId, AlertType alertType);

    Page<TransactionAlertLogs> findByAlertTimeBetween(LocalDateTime start, LocalDateTime end, Pageable pageable);

    List<TransactionAlertLogs> findByAlertTimeBetween(LocalDateTime startAlertTime, LocalDateTime endAlertTime);

    Page<TransactionAlertLogs> findByTransactionType(TransactionType transactionType, Pageable pageable);

    Page<TransactionAlertLogs> findByAlertTimeBetweenAndTransactionType(LocalDateTime startAlertTime, LocalDateTime endAlertTime, TransactionType transactionType, Pageable pageable);

    Page<TransactionAlertLogs> findByAlertType(AlertType alertType, Pageable pageable);

    Page<TransactionAlertLogs> findByTransactionTypeAndAlertType(TransactionType transactionType, AlertType alertType, Pageable pageable);

    Page<TransactionAlertLogs> findByAlertTimeBetweenAndTransactionTypeAndAlertType(LocalDateTime startAlertTime, LocalDateTime endAlertTime, TransactionType transactionType, AlertType alertType, Pageable pageable);

    Page<TransactionAlertLogs> findByAlertTimeBetweenAndAlertType(LocalDateTime startAlertTime, LocalDateTime endAlertTime, AlertType alertType, Pageable pageable);


    @Query("SELECT COUNT(t) FROM TransactionAlertLogs t WHERE DATE(t.alertTime) = CURRENT_DATE")
    Long countTodayAlerts();

    @Query("SELECT COALESCE(SUM(t.totalAmountDuringAlert), 0) FROM TransactionAlertLogs t WHERE DATE(t.alertTime) = CURRENT_DATE")
    Double sumTodayTotalAmount();

}
