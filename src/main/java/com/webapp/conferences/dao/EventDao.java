package com.webapp.conferences.dao;

import com.webapp.conferences.exceptions.DaoException;
import com.webapp.conferences.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventDao {

    List<Event> findAll() throws DaoException;
    Optional<Event> findById(long id) throws DaoException;
    int add(Event event) throws DaoException;
    boolean update(Event event) throws DaoException;
    boolean delete(long eventId) throws DaoException;

}
