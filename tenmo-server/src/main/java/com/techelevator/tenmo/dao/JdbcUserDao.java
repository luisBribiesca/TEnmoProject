package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class JdbcUserDao implements UserDao {
    private static List<User> users = new ArrayList<>();
    private final String USER = "USER";

    private JdbcTemplate jd;

    JdbcAccountDao accDao = new JdbcAccountDao();

    public JdbcUserDao(JdbcTemplate jd) {
        this.jd = jd;
    }

    public JdbcUserDao() {
        this.jd = new Data().getJdbcTemplate();
    }

    @Override
    public int getId(String input) {
        return getUser(input).getId();
    }

    /*
    * Takes in an account Id, User Id, or a username and returns the User object that is connected to one of those 
    * if it has already been created. Returns null otherwise.
    */
    @Override
    public User getUser(String input) {
        if (Objects.isNull(input) || input.isBlank())
            throw new IllegalArgumentException("invalid input.");
        switch (inputType(input)) {
            case ("Account"):
                int id = Integer.parseInt(input);
                String sql = "SELECT * FROM tenmo_user t JOIN account a ON t.user_id = a.user_id WHERE account_id = ?";
                SqlRowSet rs = jd.queryForRowSet(sql, id);
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
                throw new UsernameNotFoundException("The account ID: " + input + " was not found.");
            case ("User"):
                id = Integer.parseInt(input);
                sql = "SELECT * FROM tenmo_user where user_id = ?";
                rs = jd.queryForRowSet(sql, id);
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
                throw new UsernameNotFoundException("The User ID: " + input + " was not found.");
            case ("Username"):
                sql = "SELECT t.user_id, t.username, t.password_hash, t.role FROM account a JOIN tenmo_user t ON a.user_id = t.user_id where username = ?;";
                rs = jd.queryForRowSet(sql, input.toLowerCase());
                if (rs.next()) {
                    return mapRowToUser(rs);
                }
                throw new UsernameNotFoundException("The username: " + input.toLowerCase() + " was not found.");

            default:
                return null;
        }
    }

    /*
     * Assigns all User objects that are already in the database into a List.
     */
    @Override
    public List<User> findAll(String input) {
        boolean isEqual = false;
        switch (input) {
            case ("Fill"):
                String sql = "SELECT user_id, username, password_hash, role FROM tenmo_user";
                SqlRowSet results = jd.queryForRowSet(sql);
                while (results.next()) {
                    User user = mapRowToUser(results);
                    for (User u : users) {
                        if (u.equals(user)) {
                            isEqual = true;
                            break;
                        } else {
                            isEqual = false;
                        }
                    }
                    if (isEqual) {
                        continue;
                    } else {
                        users.add(user);
                    }
                }
                return users;
            case ("Pull"):
                return users;
            default:
                return users;
        }
    }

    /*
     * This method creates a user using the username and password given. This also inserts it into
     * the database if the creation of the object succeeds as well as starts on the creation of an
     * Account object.
     */
    @Override
    public boolean create(String username, String password) {
        findAll("Fill");
        if (Objects.isNull(username) || username.isBlank())
            throw new DataIntegrityViolationException("Null or empty usernames not allowed.");
        if (Objects.isNull(password) || password.isBlank())
            throw new java.lang.IllegalArgumentException("Null or empty passwords not allowed.");
        for (User u : users) { //Checking to see if the users are already created
            try {
                if (u.getUsername().equals(username)) {
                    System.out.println("Username " + username.toLowerCase() + " already exists.");
                    throw new DataIntegrityViolationException("Username already exists.");
                }
            } catch (DataIntegrityViolationException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
        // create user
        String sql = "INSERT INTO tenmo_user (username, password_hash, role) VALUES (?, ?, ?) RETURNING user_id;";
        String password_hash = new BCryptPasswordEncoder().encode(password);
        Integer newUserId;
        newUserId = jd.queryForObject(sql, Integer.class, username.toLowerCase(), password_hash, USER);

        if (newUserId == null)
            return false;
        // create account
        System.out.println("Created " + username.toLowerCase());
        User newUser = new User();
        newUser.setActivated(true);
        newUser.setAuthorities(USER);
        newUser.setId(newUserId);
        newUser.setUsername(username.toLowerCase());
        newUser.setPassword(password_hash);
        users.add(newUser);
        accDao.create(newUserId);
        return true;
    }

    /*
     * This method removes an account from the database and from the static users ArrayList.
     */
    public boolean remove(User user) {
        try {
            if (!Objects.isNull(getUser(user.getUsername()))) {
                if (users.remove(user)) {
                    String sql = "DELETE FROM account WHERE user_id IN (SELECT user_id FROM tenmo_user WHERE username = ?)";
                    jd.update(sql, user.getUsername());
                    sql = "DELETE FROM tenmo_user WHERE username = ?;";
                    jd.update(sql, user.getUsername());
                    return true;
                }
            }
        } catch (UsernameNotFoundException e) {
            return false;
        }
        return false;

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
     * This method is a helper method to allow for quick assignment to an User object from an SqlRowSet.
     */
    public User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username").toLowerCase());
        user.setPassword(rs.getString("password_hash"));
        user.setActivated(true);
        user.setAuthorities(rs.getString("role"));
        return user;
    }
}
