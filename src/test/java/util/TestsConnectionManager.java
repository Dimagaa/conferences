package util;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.webapp.conferences.dao.ConnectionManager;
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
    private Connection connection;

    public void init() {
        Properties properties = new Properties();

        try (FileInputStream fis = new FileInputStream("src/test/resources/test-db.properties")) {
            properties.load(fis);
            MysqlDataSource ds = new MysqlDataSource();
            ds.setUrl(properties.getProperty("url"));
            ds.setUser(properties.getProperty("user"));
            ds.setPassword(properties.getProperty("password"));
            dataSource = ds;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public Connection getConnection() throws SQLException {
       if(connection == null) {
           connection = dataSource.getConnection();
           connection.setSavepoint();
       }
       return connection;
    }

    @Override
    public Connection getTransaction() {
        Connection connection = null;
        logger.trace("Start transaction");
        try {
            connection = getConnection();
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    @Override
    public void rollback(Connection connection) {
        try {
            logger.trace("Rollback transaction");
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void commit(Connection connection) {
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close(Connection connection) {
            logger.trace("Close transaction");
    }

}
