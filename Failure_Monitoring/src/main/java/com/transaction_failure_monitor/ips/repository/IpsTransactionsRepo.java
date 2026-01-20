package com.transaction_failure_monitor.ips.repository;


import com.transaction_failure_monitor.ips.entity.IpsTransactions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface IpsTransactionsRepo extends JpaRepository<IpsTransactions, Long> {
    List<IpsTransactions> findByTodayDate(String todayDate);

    Optional<IpsTransactions> findByTransid(String transid);


}
