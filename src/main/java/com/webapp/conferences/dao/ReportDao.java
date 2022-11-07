package com.webapp.conferences.dao;

import com.webapp.conferences.exceptions.DaoException;
import com.webapp.conferences.model.Report;

import java.util.List;
import java.util.Optional;

public interface ReportDao {

    List<Report> findAll() throws DaoException;

    List<Report> findByEvent(long eventId) throws DaoException;

    Optional<Report> findById(long id) throws DaoException;

    Report add(Report report) throws DaoException;

    boolean update(Report report) throws DaoException;

    boolean delete(long reportId) throws DaoException;

    boolean addAll(List<Report> reports) throws DaoException;

    List<Report> findProposedByEvent(long eventId) throws DaoException;

    boolean createReportRequest(long reportId, long speakerId) throws DaoException;

    boolean acceptSpeaker(long reportId, long speakerId) throws DaoException;

    boolean rejectSpeaker(long reportId, long speakerId) throws DaoException;
}
