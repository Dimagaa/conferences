package com.conferences.dao;

import com.conferences.exceptions.DaoException;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * {@code ConnectionManager} provides {@link Connection} and wraps it with {@link DaoException}
 */
public interface ConnectionManager {

    /**
     * Allows get {@link Connection} with the data source
     * @return {@link Connection} to the data source
     * @throws DaoException if a database access error occurs
     */
    Connection getConnection() throws DaoException;

    /**
     * Sets {@link Connection} auto-commit state false and return it
     * @return {@link Connection}
     * @throws DaoException if a database access error occurs
     */
    Connection getTransaction() throws DaoException;

    /**
     * Undoes all changes made in the current transaction.
     * @param connection {@link  Connection} instance that need  roll back
     * @throws DaoException if a database access error occurs
     */
    void rollback(Connection connection) throws DaoException;

    /**
     * Makes all changes made since the previous
     * commit/rollback permanent and releases any database locks
     * currently held by this {@code Connection} object.
     * This method should be
     * used only when auto-commit mode has been disabled.
     *  @param connection {@link Connection} instance that need commit
     * @throws DaoException if a database access error occurs
     */
    void commit(Connection connection) throws DaoException;

    /**
     * Releases this Connection object's database and JDBC resources immediately instead of waiting for them to be automatically released.
     * Calling the method close on a Connection object that is already closed is a no-op.
     * @param connection {@link Connection} that need close
     * @throws DaoException if a database access error occurs
     */
    void close(Connection connection) throws DaoException;

    /**
     * Allows set {@link DataSource}
     * @param dataSource {@link DataSource}
     */
    void setDataSource(DataSource dataSource);
}
