package com.techelevator.tenmo.model;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

public class TestForModels {
    public static final String userName = "userName";
    public static final String passWord = "safePassword";
    private UserCredentials credentials = new UserCredentials("userName","safePassword");

    private User mainUser = new User();



    @After
    public void tearDown(){
        System.out.println("Test Complete");
    }

    // This tests UserCredentials
    @Test
    public void testingUserCredentials(){
        Assert.assertEquals(userName,credentials.getUsername());
        Assert.assertEquals(passWord,credentials.getPassword());
    }

    @Test
    public void testingUserClass(){
        int myId = 1;
        String myUserName = "theUser";
        User user = new User();
        user.setId(1);
        user.setUsername("theUser");
        Assert.assertEquals(myId,user.getId());
        Assert.assertEquals(myUserName,user.getUsername());

    }

    @Test
    public void testingAuthenticatedUser(){
        AuthenticatedUser authenticated = new AuthenticatedUser();
        String theToken = "xyw123LMN";
        User user = new User();
        authenticated.setToken("xyw123LMN");
        Assert.assertEquals(theToken,authenticated.getToken());

    }

//    User mainUser = new User();

    @Test
    public void is_it_an_instance_or_not(){
        mainUser.setId(100);
        mainUser.setUsername("The Main One");

        int id = mainUser.getId();
        String userName = mainUser.getUsername();

        User otherUser= new User();
        otherUser.setId(id);
        otherUser.setUsername(userName);

        Assert.assertTrue(mainUser.equals(otherUser));



    }
}
