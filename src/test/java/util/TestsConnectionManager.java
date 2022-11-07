package util;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.webapp.conferences.dao.ConnectionManager;
import com.webapp.conferences.exceptions.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import javax.sql.DataSource;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class TestsConnectionManager implements ConnectionManager {
    Logger logger = LogManager.getLogger("Global");
    private DataSource dataSource;

    public void init() throws DaoException {
        Properties properties = new Properties();

        try (FileInputStream fis = new FileInputStream("src/test/resources/test-db.properties")) {
            properties.load(fis);
            MysqlDataSource ds = new MysqlDataSource();
            ds.setUrl(properties.getProperty("url"));
            ds.setUser(properties.getProperty("user"));
            ds.setPassword(properties.getProperty("password"));
            dataSource = ds;
        } catch (IOException e) {
            throw new DaoException("Data Source not initialized");
        }
    }
    public Connection getConnection() throws DaoException {
       try {
           return  dataSource.getConnection();
       } catch (SQLException e) {
            throw new DaoException("Getting connection failed ", e);
       }
    }

    @Override
    public Connection getTransaction() throws DaoException {
        Connection connection;
        logger.trace("Get transaction");
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DaoException("Transaction not received", e);
        }
        return connection;
    }

    @Override
    public void rollback(Connection connection) throws DaoException {
        try {
            logger.trace("Rollback transaction");
            connection.rollback();
        } catch (SQLException e) {
            throw new DaoException("Rollback failed", e);
        }
    }

    @Override
    public void commit(Connection connection) throws DaoException {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new DaoException("Commit failed", e);
        }
    }

    @Override
    public void close(Connection connection) throws DaoException {
        logger.trace("Close transaction");
        if(connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DaoException("Closing connection failed", e);
            }
        }
    }

    @Override
    public void setDataSource(DataSource dataSource) {

    }

}
