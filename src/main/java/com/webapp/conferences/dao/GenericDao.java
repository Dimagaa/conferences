package com.webapp.conferences.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public abstract class GenericDao<T> {

    protected List<T> findAll(String query) throws Exception {
        throw new UnsupportedOperationException();
    }

    @SafeVarargs
    protected final <V> List<T> findByFields(String query, V... values) throws Exception {
        throw new UnsupportedOperationException();
    }

    protected int add(String query, T item) throws  Exception {
        throw new UnsupportedOperationException();
    }

    protected <V> void updateByField(String query, T item, V value) {
        throw  new UnsupportedOperationException();
    }

    protected <V> void deleteByField(String query, V value) throws Exception {
        throw new UnsupportedOperationException();
    }

    private void closeResources(PreparedStatement statement, ResultSet rs) throws Exception{
        try {
            if (statement != null) {
                statement.close();
            }
            if(rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            throw new SQLException("Can't close resources", e);
        }
    }

    protected abstract T mapFromDataSource(ResultSet rs) throws Exception;

    protected abstract void mapToDataSource(PreparedStatement statement, T obj) throws Exception;

}
