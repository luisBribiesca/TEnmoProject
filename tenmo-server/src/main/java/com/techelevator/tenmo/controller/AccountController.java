package com.techelevator.tenmo.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    JdbcAccountDao accDao = new JdbcAccountDao();

    @GetMapping("/{input}")
    public Account get(@PathVariable String input) {
        Account acc = accDao.get(input);

        return acc;
    }

    @GetMapping("")
    public List<Account> allAccounts() {
        List<Account> all = accDao.listAll();
        return all;
    }

    @PostMapping("")
    public Account create(@RequestBody @Valid Account acc) throws Exception {
        if (!(acc == null)) {
            return accDao.create(acc);
        }
        throw new Exception();
    }

    @PutMapping("/{input}")
    public boolean setBalance(@RequestBody @Valid Account acc) {
        accDao.setBalance(acc);
        return true;
    }
}
