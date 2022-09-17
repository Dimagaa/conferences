package com.webapp.conferences.dao;

import com.webapp.conferences.exceptions.DaoException;
import com.webapp.conferences.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> getUserByLogin(String login) throws DaoException;
    List<User> findAll() throws DaoException;
    Optional<User> findById(long id) throws DaoException;
    int add(User user) throws DaoException;
    boolean update(User user) throws DaoException;
    boolean delete(long userId) throws DaoException;

}
