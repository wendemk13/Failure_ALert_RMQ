package com.transaction_failure_monitor.ips.repository;

import com.transaction_failure_monitor.ips.entity.TransactionAlerts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlertsRepo extends JpaRepository<TransactionAlerts, Integer> {
    List<TransactionAlerts> findByStatus(String status);
}
