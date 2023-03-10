package com.techelevator.tenmo.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.techelevator.tenmo.dao.JdbcTransferDao;

public class Transfer {
    @JsonProperty("transfer_id")
    private int id;
    @JsonProperty("transfer_type_id")
    private int type;
    @JsonProperty("transfer_status_id")
    private int status;
    @JsonProperty("account_from")
    private int from;
    @JsonProperty("account_to")
    private int to;
    @JsonProperty("amount")
    private BigDecimal amount;
    JdbcTransferDao transDao = new JdbcTransferDao();

    public Transfer(int type, int from, int to, BigDecimal amount) {
        this.id = 0;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.type = type;
        switch (type) {
            case (1): // Type = Request
                this.status = 1; // Status = Pending
                break;
            case (2): // Type = Send
                this.status = 2; // Status = Approved
                break;
            default:
                this.status = 3;// Status = Rejected
        }
    }

    public Transfer() {
    }

    public boolean equals(Transfer t) {
        if (!(t.getId() == this.id)) {
            return false;
        }
        if (!(t.getFrom() == (this.from))) {
            return false;
        }
        if (!(t.getTo() == (this.to))) {
            return false;
        }
        if (!(t.getStatus() == this.status)) {
            return false;
        }
        if (!(t.getType() == this.type)) {
            return false;
        }
        return true;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
