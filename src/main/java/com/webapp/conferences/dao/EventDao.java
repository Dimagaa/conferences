package com.webapp.conferences.dao;

import com.webapp.conferences.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventDao {

    List<Event> findAll() throws  Exception;
    Optional<Event> findById(long id) throws Exception;
    void add(Event event) throws Exception;
    void update(Event event) throws Exception;
    void delete(long eventId) throws Exception;

}
