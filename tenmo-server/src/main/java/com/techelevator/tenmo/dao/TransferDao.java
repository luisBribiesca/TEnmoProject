package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.List;
import com.techelevator.tenmo.model.Transfer;

public interface TransferDao {

    List<Transfer> getAll();

    List<Transfer> get(String input);

    boolean create(int type, int from, int to, BigDecimal amt);
}
