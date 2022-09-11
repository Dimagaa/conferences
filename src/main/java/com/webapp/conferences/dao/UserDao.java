package com.webapp.conferences.dao;

import com.webapp.conferences.model.User;

import java.util.Optional;

public interface UserDao {

    Optional<User> getUserByLogin(String login);
    void addUser(long id, String login, String password, User.ROLE role);
    boolean addUser(User user);

}
