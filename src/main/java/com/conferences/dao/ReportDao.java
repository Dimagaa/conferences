package com.conferences.dao;

import com.conferences.model.Event;
import com.conferences.model.Report;
import com.conferences.exceptions.DaoException;

import java.util.List;
import java.util.Optional;

/**
 * Data access object class provides specific data operations without
 * exposing details to database for an {@link Report} model.
 */
public interface ReportDao {

    /**
     * Finds all {@link Report} objects in the database
     * @return {@link List} of {@code Report} objects
     * @throws DaoException if a database access error occurs
     */
    List<Report> findAll() throws DaoException;

    /**
     * Finds all {@link Report} objects that associate with {@link Event} object
     * @param eventId {@code Event} identifier value
     * @return {@link List} of {@code Report} objects that match
     * @throws DaoException if a database access error occurs
     */
    List<Report> findByEvent(long eventId) throws DaoException;

    /**
     * Finds {@link Report} object by identifier
     * @param id {@code Report} object identifier value
     * @return {@link Optional} with {@code Report} object if found, otherwise {@code Optional} empty
     * @throws DaoException if a database access error occurs
     */
    Optional<Report> findById(long id) throws DaoException;

    /**
     * Adds {@link Report} object to the database
     * @param report {@code Report} object to be added
     * @return {@code Report} object that has updated identifier received from a database
     * @throws DaoException if a database error occurs
     */
    Report add(Report report) throws DaoException;

    /**
     * Updates {@link Report} object in the database
     * @param report updated {@code Report} object
     * @return {@code true} if {@code Report} was updated, otherwise false
     * @throws DaoException if a database access error occurs
     */
    boolean update(Report report) throws DaoException;

    /**
     * Removes the {@link Report} object corresponding to the identifier
     * @param reportId {@code Report} object identifier value
     * @return {@code  true} if {@code Report} was deleted, otherwise false
     * @throws DaoException if a database access error occurs
     */
    boolean delete(long reportId) throws DaoException;

    /**
     * Allows to add all {@code Report} objects to the database
     * @param reports {@link List} of {@code Report} objects to be added
     * @return {@code true} if {@code Report} objects was added, otherwise false
     * @throws DaoException if a database access error occurs
     */
    boolean addAll(List<Report> reports) throws DaoException;

    /**
     * Finds {@link Report} objects that proposed by speaker in the database
     * @param eventId {@code Event} identifier value
     * @return {@link List} of {@code Report} objects that match
     * @throws DaoException if a database access error occurs
     */
    List<Report> findProposedByEvent(long eventId) throws DaoException;

    /**
     * Allows speakers to request participate in the {@code Report}
     * @param reportId {@code Report} object identifier value
     * @param speakerId {@code User} object identifier value
     * @return {@code true} if a requested was created. otherwise false
     * @throws DaoException if a database access error occurs
     */
    boolean createReportRequest(long reportId, long speakerId) throws DaoException;

    /**
     * Allows to accept participate speaker in the {@code Report}
     * @param reportId {@code Report} object identifier value
     * @param speakerId {@code User} object identifier to be accepted
     * @return {@code true} if a speaker was accepted. otherwise false
     * @throws DaoException if a database access error occurs
     */
    boolean acceptSpeaker(long reportId, long speakerId) throws DaoException;

    /**
     * Allows to reject speaker request to participate in the {@code Report}
     * @param reportId {@code Report} object identifier value
     * @param speakerId {@code User} object identifier value to be rejected
     * @return {@code true} if a speaker was rejected, otherwise false
     * @throws DaoException if a database error occurs
     */
    boolean rejectSpeaker(long reportId, long speakerId) throws DaoException;

    /**
     * Allows to reject proposed {@link Report} objects.
     * Proposed {@code Report} will be removed
     * @param reportId {@code Report} object identifier value
     * @return {@code true} if a proposed report was removed, otherwise false
     * @throws DaoException if a database access error occurs
     */
    boolean rejectReport(long reportId) throws  DaoException;

    /**
     * Removed all proposed {@link Report} objects that were proposed for {@link Event} object
     * @param eventId {@code Event} object identifier value
     * @return {@code true} if proposed {@code Report} objects were deleted
     * @throws DaoException if a database access error occurs
     */
    boolean deleteALlProposed(long eventId) throws DaoException;

    /**
     * Allows find all unconfirmed {@link Report} objects for speaker
     * @param speakerId {@code User} object identifier value
     * @return {@link List} of {@code Report} object that match
     * @throws DaoException if a database access error occurs
     */
    List<Report> findUnconfirmedBySpeaker(long speakerId) throws DaoException;

    /**
     * Allows updates {@link Report} object {@link com.conferences.model.Report.Status}
     * @param reportId {@code Report} object identifier value
     * @param status {@link com.conferences.model.Report.Status} to be set
     * @return {@code true} if {@code Status} was updated
     * @throws DaoException if a database access error occurs
     */
    boolean updateStatus(long reportId, Report.Status status) throws  DaoException;
}
