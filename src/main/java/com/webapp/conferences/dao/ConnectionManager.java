package com.webapp.conferences.dao;

import com.webapp.conferences.exceptions.DaoException;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionManager {

    Connection getConnection() throws DaoException, SQLException;

    Connection getTransaction() throws DaoException;
    void rollback(Connection connection) throws DaoException;
    void commit(Connection connection) throws DaoException;
    void close(Connection connection) throws DaoException;
}
