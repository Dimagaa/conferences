package com.webapp.conferences.dao;

import com.webapp.conferences.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventDao {

    List<Event> findAll() throws  DAOException;
    Optional<Event> findById(long id) throws DAOException;
    void add(Event event) throws DAOException;
    void update(Event event) throws DAOException;
    void delete(long eventId) throws DAOException;

}
