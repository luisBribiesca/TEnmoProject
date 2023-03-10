package com.techelevator.dao;

import com.techelevator.tenmo.dao.Data;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JdbcTransferDaoTests extends BaseDaoTests {

    JdbcTemplate jd = new Data().getJdbcTemplate();
    private Transfer testTransfer;
    private Transfer testTransfer2;
    private JdbcTransferDao sut;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setup() {
        sut = new JdbcTransferDao();
    }

    @AfterEach
    //    @AfterAll
    public void onShuttingDown() {
        jd.update("DELETE FROM transfer;");
        jd.update("alter sequence seq_transfer_id  restart with 3001;");

    }

    @Test(expected = IllegalArgumentException.class)
    public void findTransfer_given_null_throws_exception() {
        sut.get(null);
    }

    @Test
    public void new_transfer_is_created() {

        boolean correctId = (sut.create(1, 2001, 2002, new BigDecimal("300.00")));
        Assert.assertTrue(correctId);
    }

    @Test
    public void remove_a_transfer_fails() {
        int transferToBeErasedById = 3001;
        Assert.assertFalse(sut.remove(transferToBeErasedById));
    }

//    @Test
//    public void remove_a_transfer_succeeds() {
//        boolean erased = true;
//        Assert.assertEquals(erased, sut.remove(3001));
//    }

    @Test
    public void makes_sure_input_is_correct() {
        String[] accountInputs = { "User", "Account", "Transfer" };
        String[] numericInput = { "1000", "2000", "3000" };

        Assert.assertEquals(accountInputs[0], sut.inputType(numericInput[0]));
        Assert.assertEquals(accountInputs[1], sut.inputType(numericInput[1]));
        Assert.assertEquals(accountInputs[2], sut.inputType(numericInput[2]));

    }

    @Test
    public void returns_transfers_by_input() {
        List<Transfer> transfer = sut.getAll();
        Assert.assertNotNull(transfer);
        int actual = transfer.size();
        Assert.assertEquals(actual, transfer.size());
        showSmallMessage1();
        System.out.println("Actual Size of the Transfer List is: " + actual);
        showSmallMessage2();

    }

    @Test
    public void making_sure_transfers_are_not_null_and_returned_by_specific_id() {
        String[] userIdArray = { "1001", "1002" };
        String[] accountIdArray = { "2001", "2002" };
        String[] transferIdArray = { "3001", "3002" };
        List<Transfer> tranList = sut.get("3001");
        List<Transfer> transfers = sut.get(transferIdArray[0]);
        Assert.assertNotNull(sut.get(userIdArray[0]));
        Assert.assertNotNull(sut.get(accountIdArray[0]));
        Assert.assertNotNull(sut.get(transferIdArray[0]));
        Assert.assertNotSame("This should not be the same", tranList, transfers);
        showSmallMessage1();
        System.out.println(tranList);
        showSmallMessage2();

    }

    public void showSmallMessage1() {
        System.out.println("================================================");
        System.out.println("================================================");
        System.out.println("===============*****=>>>><<<<=*****=============");
        System.out.println("\n");

    }

    public void showSmallMessage2() {
        System.out.println("===============*****=>>>><<<<=*****=============");
        System.out.println("================================================");
        System.out.println("================================================");

    }
}
