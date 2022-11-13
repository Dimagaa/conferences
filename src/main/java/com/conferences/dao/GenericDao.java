package com.conferences.dao;

import com.conferences.exceptions.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public abstract class GenericDao<T> {

    protected final ConnectionManager connectionManager;
    protected final Logger logger = LogManager.getLogger("Global");

    protected GenericDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    protected List<T> findAll( Connection connection, String query) throws DaoException {
        List<T> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(query);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(getEntity(resultSet));
            }
            logger.debug("Method findAll() was executed");
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            closeResources(resultSet, statement);
        }
        return result;
    }

    @SafeVarargs
    protected final <V> List<T> findByFields(Connection connection, String query, V... values) throws DaoException {
        List<T> result = new ArrayList<>();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
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
            throw new DaoException("Can't execute find all fields error was occurred", e);
        } finally {
            closeResources(resultSet, statement);
        }
        return result;
    }

    protected long add(Connection connection, String query, T item) throws  DaoException {
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            sendEntity(statement, item);
            logger.debug(statement);
            if(statement.executeUpdate() > 0) {
                resultSet = statement.getGeneratedKeys();
                if(resultSet.next()) {
                    return resultSet.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            closeResources(resultSet, statement);
        }
        throw new DaoException("Returned generated key is -1 for"+ item.toString());
    }

    protected <V> boolean updateByField(Connection connection, String query, T item, V value, int parameterIndex) throws DaoException {
        PreparedStatement statement = null;
        int result;
        try {
            statement = connection.prepareStatement(query);
            setParameter(statement, parameterIndex, value);
            sendEntity(statement, item);
            result = statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            closeResources(null, statement);
        }
        return result > 0;
    }

    @SafeVarargs
    protected final <V> boolean updateByField(Connection connection, String query, V... values) throws DaoException {
        PreparedStatement statement = null;
        int result;
        try {
            statement = connection.prepareStatement(query);
            for(int i = 0;i < values.length; i++) {
                setParameter(statement, i+1, values[i]);
            }
            result = statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            closeResources(null, statement);
        }
        return result > 0;
    }

    protected <V> boolean deleteByField(Connection connection, String query, V value) throws DaoException {
        PreparedStatement statement = null;
        int result;
        try {
            statement = connection.prepareStatement(query);
            setParameter(statement,1 , value);
            result = statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            closeResources(null, statement);
        }
        return result > 0;
    }

    protected  <V> void setParameter(PreparedStatement statement, int index, V val) throws SQLException {
        if(val.getClass() == Long.class) {
            statement.setLong(index, (Long) val);
            return;
        }
        if(val.getClass() == String.class) {
            statement.setString(index, (String) val);
            return;
        }
        if(val.getClass() == Integer.class) {
            statement.setInt(index, (Integer) val);
            return;
        }
        if(val.getClass() == Timestamp.class) {
            statement.setTimestamp(index, (Timestamp) val);
            return;
        }
        throw new IllegalArgumentException("Value for statement type is incompatible: " + val);
    }

    protected void closeResources(ResultSet rs, PreparedStatement statement) throws DaoException{
        try {
            if(rs != null) {
                rs.close();
                logger.trace("ResultSet was closed");
            }
            if (statement != null) {
                statement.close();
                logger.trace("Statement was closed");
            }
        } catch (SQLException e) {
            throw new DaoException("PreparedStatement or ResultSet can't close", e);
        }
    }

    protected abstract T getEntity(ResultSet rs) throws SQLException;

    protected abstract void sendEntity(PreparedStatement statement, T obj) throws SQLException;

}
