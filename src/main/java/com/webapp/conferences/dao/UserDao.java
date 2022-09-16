package com.webapp.conferences.dao;

import com.webapp.conferences.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> getUserByLogin(String login);
    List<User> findAll() throws Exception;
    Optional<User> findById(long id) throws Exception;
    void add(User user) throws Exception;
    void update(User user) throws Exception;
    void delete(long userId) throws Exception;



    void addUser(long id, String login, String password, User.ROLE role);
    boolean addUser(User user);
    boolean addUser(String login, String password);
    boolean isExistsUser(String login);

}
