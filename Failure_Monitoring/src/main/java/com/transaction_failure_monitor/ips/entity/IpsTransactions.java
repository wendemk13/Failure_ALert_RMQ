package com.transaction_failure_monitor.ips.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lib_ips_transactions_demo_wm")
public class IpsTransactions {

    @Column(name = "transid")
    private String transid;

    @Column(name = "debtor_account_no")
    private String debtorAccountNo;

    @Column(name = "amount")
    private String amount;

    @Column(name = "is_returned")
    private String isReturned;

    @Column(name = "created_date")
    private String createdDate;

    @Column(name = "status_updated_date")
    private String statusUpdatedDate;

    @Column(name = "creditor_account_no")
    private String creditorAccountNo;

    @Column(name = "today_date")
    private String todayDate;

    @Column(name = "debtor_bank_bic")
    private String debtorBankBic;

    @Column(name = "creditor_bank_bic")
    private String creditorBankBic;

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "transaction_status")
    private String transactionStatus;

    @Column(name = "transfer_ref_no")
    private String transferRefNo;

    @Column(name = "debtor_account_holder_name")
    private String debtorAccountHolderName;

    @Column(name = "creditor_account_holder_name")
    private String creditorAccountHolderName2;

    @Column(name = "debitor_account_holder_name")
    private String debitorAccountHolderName;

    @Column(name = "address_line")
    private String addressLine;

    @Column(name = "branch_code")
    private String branchCode;

    @Column(name = "cbs_transaction_id")
    private String cbsTransactionId;

    @Column(name = "creditor_currency")
    private String creditorCurrency;

    @Column(name = "debtor_currency")
    private String debtorCurrency;

    @Column(name = "end_to_end_id")
    private String endToEndId;

    @Column(name = "ets_to_lib_push_pay_cre_dt")
    private String etsToLibPushPayCreDt;

    @Column(name = "lib_cbs_transaction_status")
    private String libCbsTransactionStatus;

    @Column(name = "lib_cbs_transaction_status_code")
    private String libCbsTransactionStatusCode;

    @Column(name = "lib_to_ets_push_pay_cre_dt")
    private String libToEtsPushPayCreDt;

    @Column(name = "lib_to_ets_push_pay_msg_id")
    private String libToEtsPushPayMsgId;

    @Column(name = "total_amount_value")
    private String totalAmountValue;

    @Column(name = "creditor_account_number")
    private String creditorAccountNumber;

    @Column(name = "eth_switch_fee")
    private String ethSwitchFee;

    @Column(name = "ets_to_lib_acc_holder_name")
    private String etsToLibAccHolderName;

    @Column(name = "ets_to_lib_push_pay_msg_id")
    private String etsToLibPushPayMsgId;

    @Column(name = "ets_to_lib_verf_res_cre_dt_tm")
    private String etsToLibVerfResCreDtTm;

    @Column(name = "ets_to_lib_verf_res_msg_id")
    private String etsToLibVerfResMsgId;

    @Column(name = "transaction_side_type")
    private String transactionSideType;
}
