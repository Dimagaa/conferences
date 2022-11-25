package com.conferences.dao;

import com.conferences.model.Event;
import com.conferences.exceptions.DaoException;
import com.conferences.services.pagination.Page;

import java.util.List;
import java.util.Optional;

/**
 * Data access object class provides specific data operations without
 * exposing details to database for an {@link Event} model.
 */
public interface EventDao {

    /**
     * Finds all {@link Event} objects in the database
     * @return {@link List} of {@code Event} objects
     * @throws DaoException if a database access error occurs
     */
    List<Event> findAll() throws DaoException;

    /**
     * Finds {@link Event} object by identifier
     * @param id {@code Event} object identifier value
     * @return {@link Optional} with {@code Event} object if found, otherwise {@code Optional} empty
     * @throws DaoException if a database access error occurs
     */
    Optional<Event> findById(long id) throws DaoException;

    /**
     * Finds {@link Event} objects that {@code User} has been joined
     * @param userId {@code User} identifier value
     * @return {@link List} of {@code Event} objects
     * @throws DaoException if a database access error occurs
     */
    List<Event> findByParticipate(long userId) throws DaoException;

    /**
     * Adds {@link Event} object to the database
     * @param event {@code Event} object to be added
     * @return {@code Event} object that has updated identifier received from a database
     * @throws DaoException if a database access error occurs
     */
    Event add(Event event) throws DaoException;

    /**
     * Updates {@link Event} object in the database
     * @param event updated {@code Event} object
     * @return {@code true} if event was updated, otherwise false
     * @throws DaoException if a database access error occurs
     */
    boolean update(Event event) throws DaoException;

    /**
     * Updates {@link com.conferences.model.Event.Status}  in {@link  Event} object
     * based on {@code Event} identifier
     * @param eventId {@code Event} identifier value
     * @param status {@code Status} to be set
     * @return {@code true} if {@code Event} was updated
     * @throws DaoException if a database access error occurs
     */
    boolean updateStatus(long eventId, Event.Status status) throws DaoException;

    /**
     * Removes the {@link Event} object corresponding to the identifier
     * @param eventId {@code Event} identifier value
     * @return {@code true} if {@code Event} was deleted
     * @throws DaoException if a database access error occurs
     */
    boolean delete(long eventId) throws DaoException;

    /**
     * Handles {@link Page}, configures the query to the database, return found {@link Event} objects
     * @param page {@link Page} object that contains parameters for the query
     * @return {@link List} of {@code Event} objects that was found
     * @throws DaoException if a database access error occurs
     */
    List<Event> getPreparedEvents(Page page) throws DaoException;

    /**
     * Find all place from {@link Event} objects that match
     * @param prefix {@code String} that is criteria for searching
     * @return {@link List} of {@code Strings} that match
     * @throws DaoException if a database error occurs
     */
    List<String> findPlaces(String prefix) throws DaoException;

    /**
     *Allow join to {@link Event} user for participating
     * @param eventId {@code Event} identifier value
     * @param userId {@code User} identifier value
     * @return {@code true} if {@code User} was joined to {@code Event}, otherwise {@code false}
     * @throws DaoException if a database error occurs
     */
    boolean joinEvent(long eventId, long userId) throws DaoException;

    /**
     * Allows user leave {@link Event}
     * @param eventId {@code Event} identifier value
     * @param userId {@code User} identifier value
     * @return {@code true} if {@code User} leaved {@code Event}, otherwise {@code false}
     * @throws DaoException if a database access error occurs
     */
    boolean leaveEvent(long eventId, long userId) throws DaoException;

    /**
     * Checks {@code User} participate in {@code Event}
     * @param eventId {@code Event} identifier value
     * @param userId {@code User} identifier value
     * @return {@code true} if {@code User} is joined to Event, otherwise {@code false}
     * @throws DaoException if a database access error occurs
     */
    boolean isJoined(long eventId, long userId) throws DaoException;

    /**
     * Find all headers of {@link Event} that match
     * @param prefix {@code String} that is criteria for searching
     * @param isUser {@code boolean} that mark searching for role {@code User}
     * @return {@link List} of strings that match
     * @throws DaoException if a database access error occurs
     */
    List<String> getEventHeaders(String prefix, boolean isUser) throws DaoException;
}
