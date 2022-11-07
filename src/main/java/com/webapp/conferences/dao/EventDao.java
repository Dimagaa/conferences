package com.webapp.conferences.dao;

import com.webapp.conferences.exceptions.DaoException;
import com.webapp.conferences.model.Event;
import com.webapp.conferences.services.pagination.Page;

import java.util.List;
import java.util.Optional;

public interface EventDao {

    List<Event> findAll() throws DaoException;
    Optional<Event> findById(long id) throws DaoException;
    List<Event> findByParticipate(long userId) throws DaoException;
    Event add(Event event) throws DaoException;
    boolean update(Event event) throws DaoException;
    boolean updateStatus(long eventId, Event.Status status) throws DaoException;
    boolean delete(long eventId) throws DaoException;
    List<Event> getPreparedEvents(Page page) throws DaoException;
    List<String> findPlaces(String prefix) throws DaoException;
}
