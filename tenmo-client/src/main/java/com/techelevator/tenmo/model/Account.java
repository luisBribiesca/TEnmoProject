package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Account {
    @JsonProperty("account_id")
    int accountId;
    @JsonProperty("user_id")
    int userId;
    @JsonProperty("balance")
    BigDecimal balance;

    public Account(){};

    public Account(int accountId, int userId, BigDecimal balance) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUser_Id(int userId) {
        this.userId = userId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString(){
        return "Account{" + "account_id=" + accountId + ", user_id=" + userId
                + '\'' + ", balance='" + balance + '}';

    }

}
