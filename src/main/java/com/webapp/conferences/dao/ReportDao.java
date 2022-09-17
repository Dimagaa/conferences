package com.webapp.conferences.dao;

import com.webapp.conferences.exceptions.DaoException;
import com.webapp.conferences.model.Report;

import java.util.List;
import java.util.Optional;

public interface ReportDao {

    List<Report> findAll() throws DaoException;
    Optional<Report> findById(long id) throws DaoException;
    int add(Report report) throws DaoException;
    boolean update(Report report) throws DaoException;
    boolean delete(long reportId) throws DaoException;
}
