package com.webapp.conferences.dao;

import com.webapp.conferences.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> getUserByLogin(String login) throws DAOException;
    List<User> findAll() throws  DAOException;
    Optional<User> findById(long id) throws DAOException;
    void add(User user) throws DAOException;
    void update(User user) throws DAOException;
    void delete(long userId) throws DAOException;



    void addUser(long id, String login, String password, User.ROLE role);
    boolean addUser(User user);
    boolean addUser(String login, String password);
    boolean isExistsUser(String login);

}
