package com.conferences.dao;

import com.conferences.exceptions.DaoException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic abstract class that provides CRUD operations and can be extended
 * to make individual DAOs. A basic object specific DAO is created by extending {@code GenericDao}  and
 * specifying the object type with genetic type parameters. For implementation should override {@link #getEntity(ResultSet)}
 * and {@link #sendEntity(PreparedStatement, Object)} methods
 * @param <T> the type of basic object specific DAO
 */
public abstract class GenericDao<T> {

    protected final Logger logger = LogManager.getLogger("Global");

    /**
     * Handles a query for finds basic objects in the database
     * @param connection a {@link Connection} object for session with a specific database
     * @param query a SQL query to the database
     * @return {@link List} with found objects
     * @throws DaoException if a database access error occurs
     */
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

    /**
     * Handles a query and finds basic object by specific field
     * @param connection a {@link Connection} object for session with a specific database
     * @param query a SQL query to the database
     * @param values parameter values that will set
     * @return {@link List} of object that was found
     * @param <V> a type of designed parameter
     * @throws DaoException if a database access error occurs
     */
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

    /**
     * Adds the designed items to the database
     * @param connection a {@link Connection} object for session with a specific database
     * @param query a SQL query to the database
     * @param item the object to be added
     * @return generated key
     * @throws DaoException if a database access error occurs
     */
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

    /**
     * Updates the existing object in the database
     * @param connection a {@link Connection} object for session with a specific database
     * @param query a SQL query to the database
     * @param item the object to be updated
     * @param value parameter value for the condition of determining rows to update
     * @param parameterIndex index of parameter for the condition of determining rows to update
     * @return true if the row count for SQL DML statements more than 0, otherwise false
     * @param <V> type of parameter for the condition of determining rows to update
     * @throws DaoException if a database access error occurs
     */
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

    /**
     * Allows updates specific rows by designed parameters
     * @param connection a {@link Connection} object for session with a specific database
     * @param query a SQL query to the database
     * @param values parameter values
     * @return {@code true} if the row count more than 0, otherwise {@code false}
     * @param <V> type of parameter
     * @throws DaoException if a database access error occurs
     */
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

    /**
     * Allows to delete rows by the specific parameters
     * @param connection a {@link Connection} object for a session with a specific database
     * @param query a SQL query to the database
     * @param value the parameter value
     * @return {@code true} if the deleted row count more than 0, otherwise {@code false}
     * @param <V> type of parameter
     * @throws DaoException if a database access error occurs
     */
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

    /**
     * Set the designed parameter by class
     * @param statement a precompiled SQL statement
     * @param index a parameter position in  a SQL statement
     * @param val a parameter value
     * @param <V> type of parameter
     * @throws SQLException if parameterIndex does not correspond to a parameter
     *       marker in the SQL statement; if a database access error occurs or
     *       this method is called on a closed {@code PreparedStatement}
     */
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

    /**
     * Allows to close {@link ResultSet} and {@link PreparedStatement} if resources were exist, otherwise do nothing
     * @param rs a {@link ResultSet} object to be closed
     * @param statement a {@link PreparedStatement} object to be closed
     * @throws DaoException if a database access error occurs
     */
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

    /**
     * Converts data to object from given {@link ResultSet} object
     * @param rs a {@link ResultSet} object to be handled
     * @return object that was converted
     * @throws SQLException if a database access error occurs
     */
    protected abstract T getEntity(ResultSet rs) throws SQLException;

    /**
     * Converts data from object and sets parameter for a {@link PreparedStatement} object
     * @param statement {@link PreparedStatement} object to be executed
     * @param obj a object to be converted
     * @throws SQLException if a database access error occurs
     */
    protected abstract void sendEntity(PreparedStatement statement, T obj) throws SQLException;

}
