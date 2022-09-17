package com.webapp.conferences.dao.impl.mysql;

import com.webapp.conferences.dao.ConnectionManager;
import com.webapp.conferences.dao.GenericDao;
import com.webapp.conferences.dao.UserDao;
import com.webapp.conferences.exceptions.DaoException;
import com.webapp.conferences.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class MySQLUserDao extends GenericDao<User> implements UserDao {

    private final Logger logger = LogManager.getLogger("Global");
    protected MySQLUserDao() {
        super(MySQLConnectionManager.getInstance());
        logger.trace("MySQL USerDao created");
    }

    public MySQLUserDao(ConnectionManager connectionManager) {
        super(connectionManager);
        logger.trace("MySQL USerDao created");
    }

    @Override
    public Optional<User> getUserByLogin(String login) throws DaoException {
        logger.trace("Getting user by login");
        List<User> result = findByFields("SELECT * FROM users WHERE login = ?", login);

        return result.isEmpty() ?  Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public List<User> findAll() throws DaoException {
        logger.trace("Finding all users");
        return findAll("SELECT * FROM users");
    }

    @Override
    public Optional<User> findById(long id) throws DaoException {
        logger.trace("Finding user by id");
        List<User> result = findByFields("SELECT * FROM users WHERE id = ?", id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public int add(User user) throws DaoException {
        logger.trace("Adding user");
        int genId = add("INSERT INTO users(login, password, first_name, last_name, role) VALUES(?, ?, ?, ?, ?)", user);
        user.setId(genId);
        return genId;
    }

    @Override
    public boolean update(User user) throws DaoException {
        logger.trace("Updating user");
        return updateByField("UPDATE users SET login=?, password=?, first_name=?, last_name=?, role=? WHERE id=?", user, user.getId(), 6);
    }

    @Override
    public boolean delete(long userId) throws DaoException {
        logger.trace("Deleting user by id " + userId);
        return deleteByField("DELETE FROM users WHERE id = ?", userId);
    }

    @Override
    protected User getEntity(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setLogin(rs.getString("login"));
        user.setPassword(rs.getString("password"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setRole(User.ROLE.valueOf(rs.getString("role")));
        return user;
    }

    @Override
    protected void sendEntity(PreparedStatement statement, User user) throws SQLException {
        statement.setString(1, user.getLogin());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getFirstName());
        statement.setString(4, user.getLastName());
        statement.setString(5, user.getRole().name());
    }
}
