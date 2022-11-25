package com.conferences.dao;

import com.conferences.exceptions.DaoException;
import com.conferences.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Data access object class provides specific data operations without
 * exposing details to database for an {@link User} model.
 */
public interface UserDao {

    /**
     * Finds {@link User} object by login
     * @param login {@code User} uniq object property
     * @return {@link Optional} with {@code User} if found it, otherwise empty {@code Optional}
     * @throws DaoException if a database access error occurs
     */
    Optional<User> getUserByLogin(String login) throws DaoException;

    /**
     * Finds all {@link User} objects in the database
     * @return {@link List} of {@code User} object
     * @throws DaoException if a database access error occurs
     */
    List<User> findAll() throws DaoException;

    /**
     * Finds {@link User} object by identifier
     * @param id {@code User} object identifier value
     * @return {@link Optional} with {@code User} object if found it, otherwise false
     * @throws DaoException if a database access error occurs
     */
    Optional<User> findById(long id) throws DaoException;

    /**
     * Finds all {@link User} objects that were joined for the {@code  Event}
     * @param event_id {@code Event} object identifier value
     * @return {@link List} of {@code User} that match
     * @throws DaoException if a database access error occurs
     */
    List<User> findParticipants(long event_id) throws DaoException;

    /**
     * Adds new {@link User} object in the database
     * @param user {@code User} object to be added
     * @return {@code User} object that has updated identifier received from the database
     * @throws DaoException if a database access error occurs
     */
    User add(User user) throws DaoException;

    /**
     * Updates {@link User} object in the database
     * @param user updated {@code User} object
     * @return {@code true} if {@code Report} was updated, otherwise false
     * @throws DaoException if a database access error occurs
     */
    boolean update(User user) throws DaoException;

    /**
     * Removes {@link User} object from the database
     * @param userId {@code User} object identifier value
     * @return {@code true} if {@code User} was deleted, otherwise {@code false}
     * @throws DaoException if a database access error occurs
     */
    boolean delete(long userId) throws DaoException;

    /**
     * Finds all {@link User} objects by {@link com.conferences.model.User.Role}
     * @param role {@code User} object {@code Role}
     * @return {@link List} of {@code User} objects that match
     * @throws DaoException if a database access error occurs
     */
    List<User> findUsersByRole(User.Role role) throws DaoException;

    /**
     * Finds all {@link User} objects that requested participate in the {@code Report}
     * @param reportId {@code Report} object identifier value
     * @return {@link List} of {@code User} objects that match
     * @throws DaoException if a database access error occurs
     */
    List<User> findProposedSpeaker(long reportId) throws DaoException;
}
