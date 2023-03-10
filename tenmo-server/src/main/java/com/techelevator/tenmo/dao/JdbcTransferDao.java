package com.techelevator.tenmo.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;

public class JdbcTransferDao implements TransferDao {
    public static List<Transfer> transfers = new ArrayList<Transfer>();
    JdbcAccountDao accDao = new JdbcAccountDao();
    JdbcTemplate jt;

    public JdbcTransferDao(JdbcTemplate jt) {
        this.jt = jt;
    }

    public JdbcTransferDao() {
        this.jt = new Data().getJdbcTemplate();
    }

    /*
     * This method fills in the static transfers ArrayList each time it is called with the "Fill" String as its input.
     * This allows for quick access to the data isntead of cosntantly going back into the database. It returns 
     * every transfer made.
     */
    @Override
    public List<Transfer> getAll() {
        boolean isEqual = false;
        String sql = "SELECT * FROM transfer";
        SqlRowSet results = jt.queryForRowSet(sql);
        while (results.next()) {
            Transfer transfer = mapRowToTransfer(results);
            for (Transfer t : transfers) {
                if (t.equals(transfer)) {
                    isEqual = true;
                    break;
                } else {
                    isEqual = false;
                }
            }
            if (isEqual) {
                continue;
            } else {
                transfers.add(transfer);
            }
        }
        return transfers;
    }

    /*
     * This method is a helper method primarily to swiftly create a Transfer object from an SqlRowSet.
     */
    private Transfer mapRowToTransfer(SqlRowSet results) {
        Transfer trans = new Transfer();
        trans.setId(results.getInt("transfer_id"));
        trans.setAmount(results.getBigDecimal("amount"));
        trans.setFrom(results.getInt("account_from"));
        trans.setTo(results.getInt("account_to"));
        trans.setType(results.getInt("transfer_type_id"));
        trans.setStatus(results.getInt("transfer_status_id"));
        return trans;

    }

    /*
    * This method returns a List of all the transfers its Account object is included in. You may find the list
    * of transfers an account is included in by inserting a username, user ID, account ID, or transaction number
    * into the input String that it accepts.
    */
    public List<Transfer> get(String input) {
        List<Transfer> listing = new ArrayList<Transfer>();
        if (Objects.isNull(input) || input.isBlank())
            throw new IllegalArgumentException("invalid input.");
        switch (inputType(input)) {
            case ("Transfer"): //Looking for the transaction with that transaction ID
                int id = Integer.parseInt(input);
                String sql = "SELECT * FROM transfer WHERE transfer_id = ?";
                SqlRowSet rs = jt.queryForRowSet(sql, id);
                if (rs.next()) {
                    listing.add(mapRowToTransfer(rs));
                    return listing;
                }
                throw new UsernameNotFoundException("The transfer ID: " + input + " was not found.");
            case ("Account"): // Joins the account and transfer tables using transfer's account_from
                id = Integer.parseInt(input); // to find all of the transactions with that account ID. (Hindsight:no need to join)
                sql = "SELECT * FROM transfer t JOIN account a ON t.account_from = a.account_id WHERE account_id = ?";
                rs = jt.queryForRowSet(sql, id);
                while (rs.next()) {
                    listing.add(mapRowToTransfer(rs)); // Joins the account and transfer tables using transfer's account_to
                } // to find all of the transactions with that account ID. (Hindsight:no need to join)
                sql = "SELECT * FROM transfer t JOIN account a ON t.account_to = a.account_id WHERE account_id = ?";
                rs = jt.queryForRowSet(sql, id);
                while (rs.next()) {
                    listing.add(mapRowToTransfer(rs));
                }
                if (!(listing.isEmpty())) {
                    return listing;
                }
                throw new UsernameNotFoundException("The account ID: " + input + " was not found.");
            case ("User"): // Joins the transfer,account, and tenmo_user tables to find all of the
                id = Integer.parseInt(input); // transactions with the specific user in it. (Hindsight:no need to join all of the tables)     
                sql = "select u.user_id, t.transfer_id, t.amount, t.account_from, t.account_to, t.transfer_type_id,t.transfer_status_id from tenmo_user u join (account a join transfer t ON t.account_from = a.account_id) ON a.user_id = u.user_id WHERE a.user_id = ?;";
                rs = jt.queryForRowSet(sql, id);
                while (rs.next()) {
                    listing.add(mapRowToTransfer(rs));
                }
                sql = "select u.user_id, t.transfer_id, t.amount, t.account_from, t.account_to, t.transfer_type_id,t.transfer_status_id from tenmo_user u join (account a join transfer t ON t.account_to = a.account_id) ON a.user_id = u.user_id where a.user_id = ?;";
                rs = jt.queryForRowSet(sql, id);
                while (rs.next()) {
                    listing.add(mapRowToTransfer(rs));
                }
                if (!(listing.isEmpty())) {
                    return listing;
                }
                throw new UsernameNotFoundException("The User ID: " + input + " was not found.");
            case ("Username"):
                sql = "select u.user_id, t.transfer_id, t.amount, t.account_from, t.account_to, t.transfer_type_id,t.transfer_status_id from tenmo_user u join (account a join transfer t ON t.account_to = a.account_id) ON a.user_id = u.user_id where u.username = ?;";
                rs = jt.queryForRowSet(sql, input); // Joins the transfer,account, and tenmo_user tables to find all of the
                while (rs.next()) { // transactions with the specific user found by the username in it.(Hindsight:no need to join all of the tables)
                    listing.add(mapRowToTransfer(rs));
                }
                sql = "select u.user_id, t.transfer_id, t.amount, t.account_from, t.account_to, t.transfer_type_id,t.transfer_status_id from tenmo_user u join (account a join transfer t ON t.account_to = a.account_id) ON a.user_id = u.user_id where u.username = ?;";
                rs = jt.queryForRowSet(sql, input);
                while (rs.next()) {
                    listing.add(mapRowToTransfer(rs));
                }
                if (!(listing.isEmpty())) {
                    return listing;
                }
                throw new UsernameNotFoundException("The username: " + input + " was not found.");

            default:
                return null;
        }
    }

    /*
     * A helper method for the get method primarily. Is used to distinguish between the options
     * the user is using.
     */
    public String inputType(String input) {
        try {
            if (Integer.parseInt(input) / 1000 == 3) {
                return "Transfer";
            } else if (Integer.parseInt(input) / 1000 == 2) {
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
     * This method creates a Transfer object using its type, the account ID that its from, the account ID that it's to,
     * and the amount of the transaction.
     */
    @Override
    public boolean create(int type, int from, int to, BigDecimal amt) {
        if ((type == 1 || type == 2) && !Objects.isNull(accDao.get(Integer.toString(from)))
                && !Objects.isNull(accDao.get(Integer.toString(to)))
                && (amt.compareTo(new BigDecimal(0)) > 0)) {
            Transfer trans = new Transfer(type, from, to, amt);
            String sql = "INSERT INTO transfer (transfer_type_id,transfer_status_id,account_from,account_to, amount) values (?, ?, ?, ?, ?) RETURNING transfer_id;";
            trans.setId(jt.queryForObject(sql, Integer.class, type, type, from, to, amt));
            transfers.add(trans);
            return true;
        } else {
            return false;
        }
    }

    /*
     * This method returns a list of all pending transactions.
     */
    public List<Transfer> getPending(Account acc) {
        List<Transfer> listing = getAll();
        for (int i = listing.size() - 1; i >= 0; i--) {
            if (listing.get(i).getTo() == acc.getAccount_Id()) {
            } else if (listing.get(i).getFrom() == acc.getAccount_Id()) {
            } else {
                listing.remove(i);
                continue;
            }
            if (listing.get(i).getStatus() == 2 || listing.get(i).getStatus() == 3) {
                listing.remove(i);
                continue;
            }
        }
        return listing;
    }

    /*
     * This method removes a transaction from the database and the list of all transfers.
     */
    public boolean remove(int id) {
        Transfer trans = get(Integer.toString(id)).get(0);
        if (transfers.contains(trans)) {
            jt.update("DELETE FROM transfer WHERE transfer_id = ?", trans.getId());
            transfers.remove(trans);
            return true;
        }
        return false;
    }

    /*
     * This method allows for the setting of status for a particular Transfer object. Also changes
     * it in the database.
     */
    public boolean setStatus(Transfer trans) {
        String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?;";
        jt.update(sql, trans.getStatus(), trans.getId());
        for (Transfer t : transfers) {
            if (t.getId() == trans.getId()) {
                t.setStatus(trans.getStatus());
            }
        }
        return true;
    }

    /*
     * This method returns a list of all the finished transactions (no longer pending).
     */
    public List<Transfer> getFinished(Account acc) {
        List<Transfer> listing = getAll();
        for (int i = listing.size() - 1; i >= 0; i--) {
            if (listing.get(i).getStatus() == 3 || listing.get(i).getStatus() == 2) {
                if (listing.get(i).getTo() == acc.getAccount_Id()) {
                    continue;
                } else if (listing.get(i).getFrom() == acc.getAccount_Id()) {
                    continue;
                } else {
                    listing.remove(i);
                }
            } else {
                listing.remove(i);
            }

        }
        return listing;
    }

}
