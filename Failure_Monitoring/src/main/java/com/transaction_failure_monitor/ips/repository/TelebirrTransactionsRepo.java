package com.transaction_failure_monitor.ips.repository;

import com.transaction_failure_monitor.ips.entity.TelebirrTransactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TelebirrTransactionsRepo extends JpaRepository<TelebirrTransactions, Integer> {

    List<TelebirrTransactions> findByTodayDate(String date);

    Optional<TelebirrTransactions> findByTransactionIdTelebirr(String transactionId);


}
