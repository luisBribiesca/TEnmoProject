package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class Account {
    @JsonProperty("account_id")
    int account_Id;
    @JsonProperty("user_id")
    int user_Id;
    @JsonProperty("balance")
    BigDecimal balance;

    public Account(int account_Id, int user_Id, BigDecimal balance) {
        this.account_Id = account_Id;
        this.user_Id = user_Id;
        this.balance = balance;
    }

    public Account() {

    }

    public int getAccount_Id() {
        return account_Id;
    }

    public void setAccount_Id(int account_Id) {
        this.account_Id = account_Id;
    }

    public int getUser_Id() {
        return user_Id;
    }

    public void setUser_Id(int user_Id) {
        this.user_Id = user_Id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}
