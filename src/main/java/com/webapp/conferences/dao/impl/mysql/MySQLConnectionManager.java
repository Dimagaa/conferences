package com.webapp.conferences.dao.impl.mysql;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MySQLConnectionManager {

    private static MySQLConnectionManager instance;
    private DataSource dataSource;
    private final static Logger logger = LogManager.getLogger("Global");

    private MySQLConnectionManager() {
    }

    public synchronized static MySQLConnectionManager getInstance() {
        if(instance == null) {
            instance = new MySQLConnectionManager();
        }
        return instance;
    }

    public Connection getConnection() throws SQLException {
        if(dataSource == null) {
            initDataSource();
        }
        try {
            Connection connection = dataSource.getConnection();
            logger.info("Connection is created");
            return connection;
        } catch (SQLException e) {
            logger.error("Can't get connection");
            throw new SQLException(e);
        }

    }


    private synchronized void initDataSource() throws SQLException {
        if(dataSource == null) {
            try {
                Context context = new InitialContext();
                dataSource = (DataSource) context.lookup("java:comp/env/jdbc/mysql");
                logger.trace("DataSource was initialized successful");
            } catch (NamingException e) {
                logger.error("DataSource initialization error");
                throw new SQLException(e);
            }
        }
    }
}
