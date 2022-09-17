package com.webapp.conferences.dao;

import com.webapp.conferences.exceptions.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericDao<T> {

    private final ConnectionManager connectionManager;
    private final Logger logger = LogManager.getLogger("Global");

    protected GenericDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    protected List<T> findAll(String query) throws DaoException {
        List<T> result = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {

            connection = connectionManager.getConnection();
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                result.add(getEntity(resultSet));
            }

            logger.trace("Method findAll() was executed");
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            closeResources(resultSet, statement, connection);
        }
        return result;
    }

    @SafeVarargs
    protected final <V> List<T> findByFields(String query, V... values) throws DaoException {
        List<T> result = new ArrayList<>();

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionManager.getConnection();
            statement = connection.prepareStatement(query);

            for(int i = 0;i < values.length; i++) {
               setParameter(statement, i+1, values[i]);
            }

            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(getEntity(resultSet));
            }
            logger.trace("Method findByFields() was executed");
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            closeResources(resultSet, statement, connection);
        }
        return result;
    }

    protected int add(String query, T item) throws  DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            connection = connectionManager.getTransaction();
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            sendEntity(statement, item);
            if(statement.executeUpdate() > 0) {
                connectionManager.commit(connection);
                resultSet = statement.getGeneratedKeys();
                if(resultSet.next()) {
                    return resultSet.getInt(1);
                }
                logger.trace("Method add() was executed");
            }
        } catch (SQLException e) {
            connectionManager.rollback(connection);
            throw new DaoException(e);
        } finally {
            closeResources(resultSet, statement, connection);
        }
        return -1;
    }

    protected <V> boolean updateByField(String query, T item, V value, int parameterIndex) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        int res;
        try {
            connection = connectionManager.getTransaction();
            statement = connection.prepareStatement(query);

            setParameter(statement, parameterIndex, value);

            sendEntity(statement, item);

            res = statement.executeUpdate();
            connectionManager.commit(connection);
        } catch (SQLException e) {
            connectionManager.rollback(connection);
            throw new DaoException(e);
        } finally {
            closeResources(null, statement, connection);
        }
        return res > 0;
    }

    protected <V> boolean deleteByField(String query, V value) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        int res;
        try {
            connection = connectionManager.getTransaction();
            statement = connection.prepareStatement(query);

            setParameter(statement,1 , value);
            res = statement.executeUpdate();
            connectionManager.commit(connection);
        } catch (SQLException e) {
            connectionManager.rollback(connection);
            throw new DaoException(e);
        } finally {
            closeResources(null, statement, connection);
        }

        return res > 0;
    }

    private <V> void setParameter(PreparedStatement statement, int index, V val) throws SQLException {
        if(val.getClass() == Long.class) {
            statement.setLong(index, (Long) val);
        } else if(val.getClass() == String.class) {
            statement.setString(index, (String) val);
        } else {
            throw new IllegalArgumentException("Value type is incompatible");
        }
    }

    private void closeResources(ResultSet rs, PreparedStatement statement, Connection connection) throws DaoException{
        try {
            if(rs != null) {
                rs.close();
                logger.trace("ResultSet was closed");
            }
            if (statement != null) {
                statement.close();
                logger.trace("Statement was closed");
            }
            if(connection != null) {
                connectionManager.close(connection);
                logger.trace("Connection was closed");
            }
        } catch (SQLException e) {
            throw new DaoException("PreparedStatement or ResultSet can't close", e);
        }
    }

    protected abstract T getEntity(ResultSet rs) throws SQLException;

    protected abstract void sendEntity(PreparedStatement statement, T obj) throws SQLException;

}
