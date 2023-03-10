package com.techelevator.tenmo.dao;

import java.util.List;

import com.techelevator.tenmo.model.Account;

public interface AccountDao {

    List<Account> listAll();

    Account get(String username);

    boolean create(int userId);

    public Account create(Account acc);
}
