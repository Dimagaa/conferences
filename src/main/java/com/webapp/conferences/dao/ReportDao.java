package com.webapp.conferences.dao;

import com.webapp.conferences.model.Report;

import java.util.List;
import java.util.Optional;

public interface ReportDao {

    List<Report> findAll() throws  Exception;
    Optional<Report> findById(long id) throws Exception;
    void add(Report report) throws Exception;
    void update(Report report) throws Exception;
    void delete(long reportId) throws Exception;
}
