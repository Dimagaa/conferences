package com.conferences.dao;

import com.conferences.exceptions.DaoException;
import com.conferences.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> getUserByLogin(String login) throws DaoException;
    List<User> findAll() throws DaoException;
    Optional<User> findById(long id) throws DaoException;
    List<User> findParticipants(long event_id) throws DaoException;
    User add(User user) throws DaoException;
    boolean update(User user) throws DaoException;
    boolean delete(long userId) throws DaoException;
    List<User> findUsersByRole(User.Role role) throws DaoException;
    List<User> findProposedSpeaker(long reportId) throws DaoException;
}