package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Transfer {
    @JsonProperty("transfer_id")
    private int transferId;
    @JsonProperty("transfer_type_id")
    private int transferTypeId;
    @JsonProperty("transfer_status_id")
    private int transferStatusId;
    @JsonProperty("account_from")
    private int accountFrom;
    @JsonProperty("account_to")
    private int accountTo;
    @JsonProperty("amount")
    private BigDecimal transferAmount;

    public Transfer() {
    };

    public Transfer(int transferTypeId, int accountFrom, int accountTo, BigDecimal transferAmount) {
        this.transferId = 0;
        this.transferTypeId = transferTypeId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.transferAmount = transferAmount;
        switch (transferTypeId) {
            case (1):
                this.transferStatusId = 1;
                break;
            case (2):
                this.transferStatusId = 2;
                break;
            default:
                this.transferStatusId = 3;
        }
    }

    public Transfer(int transferIdd, int transferTypeId, int accountFrom, int accountTo, BigDecimal transferAmount) {
        this.transferId = transferTypeId;
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.transferAmount = transferAmount;

    }

    /*  public Transfer(int transfer_type_id, int transfer_status_id, int account_from, int account_to, BigDecimal transferAmount) {
        this.transfer_type_id = transfer_type_id;
        this.transfer_status_id = transfer_status_id;
        this.account_from = account_from;
        this.account_to = account_to;
        this.transferAmount = transferAmount;
    } */

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(int accountFrom) {
        this.accountFrom = accountFrom;
    }

    public int getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(int accountTo) {
        this.accountTo = accountTo;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String toString() {
        return "TransferId= " + transferId + ", TransferStatus= " + transferStatusId + ", TransferType= "
                + transferTypeId;
    }
}
