package com.techelevator.tenmo.controller;

import java.util.List;
import java.util.Objects;
import java.util.zip.DataFormatException;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Transfer;

@RestController
@RequestMapping("/transfers")
public class TransferController {

    JdbcUserDao userDao = new JdbcUserDao();
    JdbcAccountDao accDao = new JdbcAccountDao();
    JdbcTransferDao transDao = new JdbcTransferDao();

    @GetMapping("/{input}")
    public Transfer[] get(@PathVariable String input) {
        List<Transfer> trans = transDao.get(input);
        return (Transfer[]) trans.toArray(new Transfer[trans.size()]);
    }

    @GetMapping("")
    public Transfer[] allAccounts() {
        List<Transfer> all = transDao.getAll();
        return all.toArray(new Transfer[all.size()]);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public boolean create(@RequestBody @Valid Transfer trans) throws DataFormatException {
        if (!(Objects.isNull(trans))) {
            return transDao.create(trans.getType(), trans.getFrom(), trans.getTo(), trans.getAmount());
        }
        throw new DataFormatException("Wrong data format.");
    }

    @GetMapping("/{input}/pending")
    public Transfer[] getPending(@PathVariable String input) {
        List<Transfer> trans = transDao.getPending(accDao.get(input));
        return trans.toArray(new Transfer[trans.size()]);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public boolean remove(@PathVariable int id) {
        return transDao.remove(id);
    }

    @PutMapping("/{id}")
    public boolean setStatus(@RequestBody @Valid Transfer trans) {
        transDao.setStatus(trans);
        return true;
    }

    @GetMapping("/{input}/completed")
    public Transfer[] getFinished(@PathVariable String input) {
        List<Transfer> trans = transDao.getFinished(accDao.get(input));
        return trans.toArray(new Transfer[trans.size()]);
    }
}
