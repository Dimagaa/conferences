package com.webapp.conferences.dao;

import com.webapp.conferences.model.Report;

import java.util.List;
import java.util.Optional;

public interface ReportDao {

    List<Report> findAll() throws  DAOException;
    Optional<Report> findById(long id) throws DAOException;
    void add(Report report) throws DAOException;
    void update(Report report) throws DAOException;
    void delete(long reportId) throws DAOException;
}
