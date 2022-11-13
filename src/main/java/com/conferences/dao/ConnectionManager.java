package com.conferences.dao;

import com.conferences.exceptions.DaoException;

import javax.sql.DataSource;
import java.sql.Connection;

public interface ConnectionManager {

    Connection getConnection() throws DaoException;

    Connection getTransaction() throws DaoException;

    void rollback(Connection connection) throws DaoException;

    void commit(Connection connection) throws DaoException;

    void close(Connection connection) throws DaoException;

    void setDataSource(DataSource dataSource);
}
