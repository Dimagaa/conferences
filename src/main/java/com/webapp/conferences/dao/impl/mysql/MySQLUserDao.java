package com.webapp.conferences.dao.impl.mysql;

import com.webapp.conferences.dao.ConnectionManager;
import com.webapp.conferences.dao.GenericDao;
import com.webapp.conferences.dao.UserDao;
import com.webapp.conferences.exceptions.DaoException;
import com.webapp.conferences.model.User;


import java.sql.*;
import java.util.List;
import java.util.Optional;

public class MySQLUserDao extends GenericDao<User> implements UserDao {

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
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            List<User> result = findByFields(connection, "SELECT * FROM users WHERE login = ?", login);
            return result.isEmpty() ?  Optional.empty() : Optional.of(result.get(0));
        } finally {
            connectionManager.close(connection);
        }
    }

    @Override
    public List<User> findAll() throws DaoException {
        logger.trace("Finding all users");
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            return findAll(connection, "SELECT * FROM users");
        } finally {
            connectionManager.close(connection);
        }
    }

    @Override
    public Optional<User> findById(long id) throws DaoException {
        logger.trace("Finding user by id");
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            List<User> result = findByFields(connection, "SELECT * FROM users WHERE id = ?", id);
            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
        } finally {
            connectionManager.close(connection);
        }
    }

    public List<User> findParticipants(long eventID) throws DaoException {
        logger.trace("FindParticipants");
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            return findByFields(connection,
                    "SELECT * FROM users WHERE id IN(SELECT user_id FROM participants WHERE event_id = ?)",
                    eventID);
        } finally {
            connectionManager.close(connection);
        }
    }

    @Override
    public User add(User user) throws DaoException {
        logger.trace("Adding user");
        Connection connection = null;
        try {
            connection = connectionManager.getTransaction();
            long genId = add(connection,
                    "INSERT INTO users(login, password, first_name, last_name, role) VALUES(?, ?, ?, ?, ?)",
                    user);
            connectionManager.commit(connection);
            user.setId(genId);
            return user;
        }catch (DaoException e) {
            connectionManager.rollback(connection);
            logger.debug("User adding failed. User: " + user, e);
            throw new DaoException("User adding failed. User: " + user, e);
        } finally {
            connectionManager.close(connection);
        }
    }

    @Override
    public boolean update(User user) throws DaoException {
        logger.trace("Updating user");
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            return updateByField(connection,
                    "UPDATE users SET login=?, password=?, first_name=?, last_name=?, role=? WHERE id=?",
                    user, user.getId(), 6);
        } finally {
            connectionManager.close(connection);
        }
    }

    @Override
    public boolean delete(long userId) throws DaoException {
        logger.trace("Deleting user by id " + userId);
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            return deleteByField(connection,"DELETE FROM users WHERE id = ?", userId);
        } finally {
            connectionManager.close(connection);
        }
    }

    @Override
    public List<User> findUsersByRole(User.Role role) throws DaoException {
        logger.trace("Find users by role");
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            return findByFields(connection, "SELECT * FROM users WHERE role = ?", role.name());
        } finally {
            connectionManager.close(connection);
        }
    }

    @Override
    public List<User> findProposedSpeaker(long reportId) throws DaoException {
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            return findByFields(connection,
                    "SELECT * FROM users WHERE id IN" +
                    "(SELECT speaker_id FROM reportRequest WHERE report_id = ?)",
                    reportId);
        } finally {
            connectionManager.close(connection);
        }
    }

    @Override
    protected User getEntity(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setLogin(rs.getString("login"));
        user.setPassword(rs.getString("password"));
        user.setFirstName(rs.getString("first_name"));
        user.setLastName(rs.getString("last_name"));
        user.setRole(User.Role.valueOf(rs.getString("role")));
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
