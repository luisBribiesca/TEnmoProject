package com.techelevator.dao;

import com.techelevator.tenmo.dao.Data;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.User;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.List;

public class JdbcUserDaoTests extends BaseDaoTests {
    protected static final User USER_1 = new User(1001, "user1", "user1", "USER");
    protected static final User USER_2 = new User(1002, "user2", "user2", "USER");
    private static final User USER_3 = new User(1003, "user3", "user3", "USER");

    private JdbcUserDao sut;

    @Before
    public void setup() {
        sut = new JdbcUserDao();
    }

    @AfterEach
    @AfterAll
    public void shutdown() {
        JdbcTemplate jd = new Data().getJdbcTemplate();
        jd.update("DELETE FROM account;");
        jd.update("DELETE FROM tenmo_user;");
        jd.update("alter sequence seq_account_id  restart with 2001;");
        jd.update("alter sequence seq_user_id  restart with 1001;");

    }

    @Test(expected = IllegalArgumentException.class)
    public void findIdByUsername_given_null_throws_exception() {
        sut.getId(null);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void findIdByUsername_given_invalid_username_throws_exception() {
        sut.getId("invalid");
    }

    @Test
    public void findIdByUsername_given_valid_user_returns_user_id() {
        int actualUserId = sut.getId(USER_1.getUsername());

        Assert.assertEquals(USER_1.getId(), actualUserId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void findByUsername_given_null_throws_exception() {
        sut.getUser(null);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void findByUsername_given_invalid_username_throws_exception() {
        sut.getId("invalid");
    }

    @Test
    public void findByUsername_given_valid_user_returns_user() {
        User actualUser = sut.getUser(USER_1.getUsername());

        Assert.assertEquals(USER_1, actualUser);
    }

    @Test
    public void getUserById_given_invalid_user_id_returns_null() {
        User actualUser = sut.getUser(Integer.toString(-1));

        Assert.assertNull(actualUser);
    }

    @Test
    public void getUserById_given_valid_user_id_returns_user() {
        User actualUser = sut.getUser(Integer.toString(USER_1.getId()));
        Assert.assertTrue(USER_1.equals(actualUser));
    }

    @Test
    public void findAll_returns_all_users() {
        List<User> users = sut.findAll("Pull");
        Assert.assertNotNull(users);
        Assert.assertEquals(3, users.size());
        Assert.assertTrue(USER_1.equals(users.get(0)));
        Assert.assertTrue(USER_2.equals(users.get(1)));
        Assert.assertTrue(USER_3.equals(users.get(2)));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void create_user_with_null_username() {
        sut.create(null, USER_3.getPassword());
    }

    @Test
    public void create_user_with_existing_username() {
        Assert.assertTrue(!sut.create(USER_1.getUsername(), USER_3.getPassword()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void create_user_with_null_password() {
        sut.create(USER_3.getUsername(), null);
    }

    @Test
    public void create_user_creates_a_user() {
        User newUser = new User(-1, "new", "user", "USER");

        boolean userWasCreated = (sut.getUser(newUser.getUsername())).isActivated();

        Assert.assertTrue(userWasCreated);

        User actualUser = sut.getUser(newUser.getUsername());
        newUser.setId(actualUser.getId());

        // actualUser.setPassword(newUser.getPassword()); // reset password back to unhashed password for testing
        Assert.assertTrue(newUser.equals(actualUser));
        sut.remove(actualUser);
    }
}
