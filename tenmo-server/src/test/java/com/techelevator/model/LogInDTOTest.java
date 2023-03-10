package com.techelevator.model;

import com.techelevator.tenmo.model.LoginDTO;
import org.junit.Assert;
import org.junit.Test;

public class LogInDTOTest {
    private final String userName = "userName";
    private final String password = "password";
    private final String notUserName = "notUserName";
    private final String notPassword = "notPassword";
    LoginDTO logIn = new LoginDTO();

    @Test
    public void testing_right_login_dto() {
        logIn.setUsername("userName");
        logIn.setPassword("password");

        Assert.assertEquals(userName, logIn.getUsername());
        Assert.assertEquals(password, logIn.getPassword());

    }

    @Test
    public void testing_that_wrong_login_does_not_work() {
        logIn.setUsername("userName");
        logIn.setPassword("password");

        Assert.assertNotEquals(notUserName, logIn.getUsername());
        Assert.assertNotEquals(notPassword, logIn.getPassword());
    }

}
