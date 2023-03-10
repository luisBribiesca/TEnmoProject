package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.util.List;

public interface UserDao {

    List<User> findAll(String input);

    User getUser(String input);

    int getId(String username);

    boolean create(String username, String password);
}
