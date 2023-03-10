package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import com.techelevator.tenmo.model.Account;

@Component
public class JdbcAccountDao implements AccountDao {

    private static final BigDecimal STARTING_BALANCE = new BigDecimal("1000.00");

    private final JdbcTemplate jt;

    public JdbcAccountDao(JdbcTemplate jt) {
        this.jt = jt;
    }

    public JdbcAccountDao() {
        this.jt = new Data().getJdbcTemplate();
    }

    /*
     * A method that returns an Account object from a multitude of search options. It accepts a String that tells it
     * what search option is being used. You can use an account ID, user ID, or username to find an account.
     */
    public Account get(String input) {
        if (Objects.isNull(input) || input.isBlank())
            throw new IllegalArgumentException("invalid input.");
        switch (inputType(input)) {
            case ("Account"):
                int id = Integer.parseInt(input);
                String sql = "SELECT * FROM account WHERE account_id = ?";
                SqlRowSet rs = jt.queryForRowSet(sql, id);
                if (rs.next()) {
                    return mapRowToAccount(rs);
                }
                throw new UsernameNotFoundException("The account ID: " + input + " was not found.");
            case ("User"):
                id = Integer.parseInt(input);
                sql = "SELECT * FROM account where user_id = ?";
                rs = jt.queryForRowSet(sql, id);
                if (rs.next()) {
                    return mapRowToAccount(rs);
                }
                throw new UsernameNotFoundException("The User ID: " + input + " was not found.");
            case ("Username"):
                sql = "SELECT a.account_id, a.user_id, a.balance, t.username FROM account a JOIN tenmo_user t ON a.user_id = t.user_id where username = ?;";
                rs = jt.queryForRowSet(sql, input);
                if (rs.next()) {
                    return mapRowToAccount(rs);
                }
                throw new UsernameNotFoundException("The username: " + input + " was not found.");

            default:
                return null;
        }

    }

    /*
     * Creates an account from scratch with its user ID as its input.
     */
    @Override
    public boolean create(int newUserId) {
        String sql = "INSERT INTO account (user_id, balance) values (?, ?);";
        try {
            jt.update(sql, newUserId, STARTING_BALANCE);
        } catch (DataAccessException e) {
            System.out.println("update didn't work.");
            return false;
        }
        return true;
    }

    /*
     * Creates an account using the account that's sent through the input.
     */
    @Override
    public Account create(Account acc) {
        String sql = "INSERT INTO account (user_id, balance) values (?, ?) RETURNING account_id;";
        try {
            int id = jt.update(sql, acc.getUser_Id(), STARTING_BALANCE);
            acc.setAccount_Id(id);
        } catch (DataAccessException e) {
            System.out.println("update didn't work.");
            return null;
        }
        return acc;
    }

    /*
     * A helper method for the get method primarily. Is used to distinguish between the options
     * the user is using.
     */
    public String inputType(String input) {
        try {
            if (Integer.parseInt(input) / 1000 == 2) {
                return "Account";
            } else if (Integer.parseInt(input) / 1000 == 1) {
                return "User";
            } else
                return "null";
        } catch (NumberFormatException e) {
            return "Username";
        } catch (Exception e) {
            return null;
        }
    }

    /*
     * This method sends a list of all Accounts that are currently made.
     */
    @Override
    public List<Account> listAll() {
        List<Account> acc = new ArrayList<>();
        String sql = "SELECT * FROM account";
        SqlRowSet rs = jt.queryForRowSet(sql);
        while (rs.next()) {
            acc.add(new Account(rs.getInt("account_id"), rs.getInt("user_id"), rs.getBigDecimal("balance")));
        }

        return acc;
    }

    /*
     * This method is a helper method to allow for quick assignment to an Account object from an SqlRowSet.
     */
    private Account mapRowToAccount(SqlRowSet result) {
        Account account = new Account(result.getInt("account_id"), result.getInt("user_id"),
                result.getBigDecimal("balance"));
        return account;
    }

    /*
     * This is a method that allows for the change in balance for an account.
     */
    public boolean setBalance(Account acc) {
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?;";
        jt.update(sql, acc.getBalance(), acc.getAccount_Id());

        return true;
    }

}
