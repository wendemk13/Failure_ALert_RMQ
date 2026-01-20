package com.transaction_failure_monitor.ips.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.Immutable;

import java.time.LocalDateTime;

@Entity
@Table(name = "telebirr_transactions_wm")
@Data
@Immutable
public class TelebirrTransactions {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "account", length = 20)
    private String account;

    @Column(name = "amount")
    private double amount;

    @Column(name = "beneficiary_msisdn", length = 20)
    private String beneficiaryMsisdn;

    @Column(name = "branch", length = 20)
    private String branch;

    @Column(name = "currency", length = 20)
    private String currency;

    @Column(name = "currency_alpha", length = 20)
    private String currencyAlpha;

    @Column(name = "event_no", length = 50)
    private String eventNo;

    @Column(name = "request_id", length = 200)
    private String requestId;

    @Column(name = "status_cancel_fr", length = 20)
    private String statusCancelFr;

    @Column(name = "status_create_fr", length = 20)
    private String statusCreateFr;

    @Column(name = "status_telebirr", length = 20)
    private String statusTelebirr;

    @Column(name = "status_transfer", length = 20)
    private String statusTransfer;

    @Column(name = "timestamp", length = 200)
    private String timestamp;

    @Column(name = "timestamp2", length = 50)
    private LocalDateTime timestamp2;

    @Column(name = "today_date", length = 100)
    private String todayDate;

    @Column(name = "transaction_id_telebirr", length = 100)
    private String transactionIdTelebirr;

    @Column(name = "transfer_ref_no", length = 100)
    private String transferRefNo;

    @Column(name = "username", length = 20)
    private String username = "Telebirr";
}
