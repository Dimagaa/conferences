package com.webapp.conferences.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class GenericDao<T> {

    protected List<T> findAll(String query) throws DAOException {
        throw new UnsupportedOperationException();
    }

    @SafeVarargs
    protected final <V> List<T> findByFields(String query, V... values) throws DAOException {
        throw new UnsupportedOperationException();
    }

    protected int add(String query, T item) throws  DAOException {
        throw new UnsupportedOperationException();
    }

    protected <V> void updateByField(String query, T item, V value) {
        throw  new UnsupportedOperationException();
    }

    protected <V> void deleteByField(String query, V value) throws DAOException {
        throw new UnsupportedOperationException();
    }

    private void closeResources(PreparedStatement statement, ResultSet rs) throws DAOException{
        try {
            if (statement != null) {
                statement.close();
            }
            if(rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            throw new DAOException("Can't close resources", e);
        }
    }

    protected abstract T mapFromDataSource(ResultSet rs) throws DAOException;

    protected abstract void mapToDataSource(PreparedStatement statement, T obj) throws DAOException;

}
