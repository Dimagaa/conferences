package com.webapp.conferences.dao.impl.mysql;

import com.webapp.conferences.dao.GenericDao;
import com.webapp.conferences.dao.UserDao;
import com.webapp.conferences.model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MySQLUserDao extends GenericDao<User> implements UserDao {

    @Override
    public Optional<User> getUserByLogin(String login) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() throws SQLException {
        List<User> users = new ArrayList<>();
        Connection con = MySQLConnectionManager.getInstance().getConnection();
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery("SELECT * FROM users");
        while (rs.next()) {
            Long id = rs.getLong(1);

        }
        return null;
    }

    @Override
    public Optional<User> findById(long id) throws Exception {
        return Optional.empty();
    }

    @Override
    public void add(User user) throws Exception {

    }

    @Override
    public void update(User user) throws Exception {

    }

    @Override
    public void delete(long userId) throws Exception {

    }

    @Override
    protected User mapFromDataSource(ResultSet rs) throws Exception {
        return null;
    }

    @Override
    protected void mapToDataSource(PreparedStatement statement, User obj) throws Exception {

    }

    @Override
    public void addUser(long id, String login, String password, User.ROLE role) {

    }

    @Override
    public boolean addUser(User user) {
        return false;
    }

    @Override
    public boolean addUser(String login, String password) {
        return false;
    }

    @Override
    public boolean isExistsUser(String login) {
        return false;
    }
}
