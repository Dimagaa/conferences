package com.webapp.conferences.dao.impl.mysql;

import com.webapp.conferences.dao.ConnectionManager;
import com.webapp.conferences.dao.EventDao;
import com.webapp.conferences.dao.GenericDao;
import com.webapp.conferences.exceptions.DaoException;
import com.webapp.conferences.model.Event;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MySQLEventDao extends GenericDao<Event> implements EventDao {

    private final Logger logger = LogManager.getLogger("Global");

    protected MySQLEventDao() {
       super(MySQLConnectionManager.getInstance());
    }

    public MySQLEventDao(ConnectionManager connectionManager) {
       super(connectionManager);
    }

    @Override
    public List<Event> findAll() throws DaoException {
        logger.trace("Finding all users");
        return findAll("SELECT * FROM events");
    }

    @Override
    public Optional<Event> findById(long id) throws DaoException {
        logger.trace("Finding by id");
        List<Event> result = findByFields("SELECT * FROM events WHERE id=?", id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public int add(Event event) throws DaoException {
        logger.trace("Adding event");
        int genId = add("INSERT INTO events(name, start_time, end_time, place, limit_events) VALUES(?, ?, ?, ?, ?)", event);
        event.setId(genId);
        return genId;
    }

    @Override
    public boolean update(Event event) throws DaoException {
        logger.trace("Updating event");
        return updateByField("UPDATE events SET name=?, start_time=?, end_time=?, place=?, limit_events=? WHERE id=?", event, event.getId(), 6);
    }

    @Override
    public boolean delete(long eventId) throws DaoException {
        logger.trace("Deleting event");
        return deleteByField("DELETE FROM events WHERE id=?", eventId);
    }

    @Override
    protected Event getEntity(ResultSet rs) throws SQLException {
        Event event = new Event();
        event.setId(rs.getLong("id"));
        event.setName(rs.getString("name"));
        event.setStartTime(rs.getTimestamp("start_time"));
        event.setEndTime(rs.getTimestamp("end_time"));
        event.setPlace(rs.getString("place"));
        event.setLimitEvents(rs.getInt("limit_events"));
        return event;
    }

    @Override
    protected void sendEntity(PreparedStatement statement, Event event) throws SQLException {
        statement.setString(1, event.getName());
        statement.setTimestamp(2, event.getStartTime());
        statement.setTimestamp(3, event.getEndTime());
        statement.setString(4, event.getPlace());
        statement.setInt(5, event.getLimitEvents());
    }
}
