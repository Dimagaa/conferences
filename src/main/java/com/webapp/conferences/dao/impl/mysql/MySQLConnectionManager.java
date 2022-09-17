package com.webapp.conferences.dao.impl.mysql;

import com.webapp.conferences.dao.ConnectionManager;
import com.webapp.conferences.exceptions.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MySQLConnectionManager implements ConnectionManager {

    private static volatile MySQLConnectionManager instance;
    private DataSource dataSource;
    private final static Logger logger = LogManager.getLogger("Global");

    private MySQLConnectionManager() {
    }

    public static MySQLConnectionManager getInstance() {
        if(instance == null) {
            synchronized (MySQLConnectionManager.class) {
                if(instance == null) {
                    instance = new MySQLConnectionManager();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() throws DaoException {
        if(dataSource == null) {
            initDataSource();
        }
        try {
            Connection connection = dataSource.getConnection();
            logger.trace("Connection ");
            return connection;
        } catch (SQLException e) {
            throw new DaoException(e);
        }

    }

    @Override
    public Connection getTransaction() throws DaoException {
        try {
            Connection connection = getConnection();
            logger.trace("Start transaction");
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void commit(Connection connection) throws DaoException {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void rollback(Connection connection) throws DaoException {
        try {
            logger.trace("Rollback transaction");
            connection.rollback();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }

    @Override
    public void close(Connection connection) throws DaoException {
        try {
            connection.setAutoCommit(true);
            connection.close();
        } catch (SQLException e) {
            throw new DaoException(e);
        }
    }
    private synchronized void initDataSource() throws DaoException {
        if(dataSource == null) {
            try {
                Context context = new InitialContext();
                dataSource = (DataSource) context.lookup("java:comp/env/jdbc/mysql");
                logger.trace("DataSource was initialized successful");
            } catch (NamingException e) {
                throw new DaoException("Data Source initialization exception, check context.xml", e);
            }
        }
    }


}