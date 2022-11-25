package com.conferences.dao.impl.mysql;

import com.conferences.dao.ConnectionManager;
import com.conferences.dao.EventDao;
import com.conferences.dao.GenericDao;
import com.conferences.dao.impl.mysql.util.MySQLQueryBuilder;
import com.conferences.dao.parameters.FilterFactory;
import com.conferences.dao.parameters.FilterParameter;
import com.conferences.dao.parameters.SortParameter;
import com.conferences.exceptions.DaoException;
import com.conferences.model.Event;
import com.conferences.services.pagination.Page;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.*;

import static com.conferences.dao.impl.mysql.util.MySQLQueryBuilder.Condition.EQUAL;

public class MySQLEventDao extends GenericDao<Event> implements EventDao {

    protected final Logger logger = LogManager.getLogger("Global");
    private final ConnectionManager connectionManager;

    public MySQLEventDao(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public List<Event> findAll() throws DaoException {
        logger.trace("Finding all users");
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            return findAll(connection, "SELECT * FROM events");
        } finally {
            connectionManager.close(connection);
        }
    }

    @Override
    public Optional<Event> findById(long eventId) throws DaoException {
        logger.trace("Find Event by id");
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            List<Event> result = findByFields(connection, "SELECT * FROM events WHERE id=?", eventId);
            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
        } finally {
            connectionManager.close(connection);
        }
    }

    @Override
    public List<Event> findByParticipate(long userId) throws DaoException {
        logger.trace("Find Event By Participate");
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            return findByFields(connection,
                    "SELECT DISTINCT events.*, users.login FROM events, participants, users WHERE events.id = participants.event_id + and users.id = ?",
                    userId);
        } finally {
            connectionManager.close(connection);
        }
    }

    @Override
    public Event add(Event event) throws DaoException {
        logger.debug("Add event");
        Connection connection = null;
        try {
            connection = connectionManager.getTransaction();
            long genId = add(connection,
                    "INSERT INTO events(name, description, start_time, end_time, place, reports_limit, status) VALUES(?, ?, ?, ?, ?, ?, ?)",
                    event);
            connectionManager.commit(connection);
            event.setId(genId);
            return event;
        } catch (DaoException e) {
            connectionManager.rollback(connection);
            logger.debug("Event not created:" + event.getName(), e.getMessage());
            throw new DaoException("Event not created: " + event.getName(), e);
        } finally {
            connectionManager.close(connection);
        }
    }

    @Override
    public boolean update(Event event) throws DaoException {
        logger.trace("Update event");
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            return updateByField(connection,
                    "UPDATE events SET name=?, description=?, start_time=?, end_time=?, place=?, reports_limit=?, status=? WHERE id=?",
                    event, event.getId(), 8);
        } finally {
            connectionManager.close(connection);
        }
    }

    @Override
    public boolean updateStatus(long eventId, Event.Status status) throws DaoException {
        logger.debug("Update event status");
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            return updateByField(connection, "UPDATE events SET status=? WHERE id=?", status.name(), eventId);
        } finally {
            connectionManager.close(connection);
        }
    }

    @Override
    public boolean delete(long eventId) throws DaoException {
        logger.debug("Deleting event");
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            return deleteByField(connection, "DELETE FROM events WHERE id=?", eventId);
        } finally {
            connectionManager.close(connection);
        }
    }

    @Override
    public List<Event> getPreparedEvents(Page page) throws DaoException {
        logger.trace("Try get prepared events");
        List<Event> events = new ArrayList<>();
        MySQLQueryBuilder builder = eventsQueryBuilder(page.getSorters(), page.getFilters())
                .limit(page.getOffset(), page.getPageSize());
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionManager.getConnection();
            statement = connection.prepareStatement(builder.getQuery());
            List<Object> values = builder.getValues();

            Iterator<Object> iterator = values.iterator();
            int i = 1;
            while (iterator.hasNext()) {
                setParameter(statement, i, iterator.next());
                i++;
            }
            resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                page.setTotalRows(0);
                return events;
            }
            page.setTotalRows(resultSet.getInt("count"));
            events.add(getEntity(resultSet));
            while (resultSet.next()) {
                events.add(getEntity(resultSet));
            }
            logger.trace("Method findByFields() was executed");
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            closeResources(resultSet, statement);
            connectionManager.close(connection);
        }
        return events;
    }

    @Override
    public List<String> findPlaces(String prefix) throws DaoException {
        List<String> result = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            connection = connectionManager.getConnection();
            statement = connection.prepareStatement("SELECT place FROM events WHERE place LIKE ?");
            statement.setString(1, prefix);
            rs = statement.executeQuery();
            while (rs.next()) {
                result.add(rs.getString("place"));
            }
        } catch (SQLException e) {
            throw new DaoException("Cannot execute method findPlace in" + this.getClass().getName(), e);
        } finally {
            closeResources(rs, statement);
            connectionManager.close(connection);
        }
        return result;
    }

    @Override
    public List<String> getEventHeaders(String prefix, boolean isUser) throws DaoException {
        List<String> headers = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            connection = connectionManager.getConnection();
            if(isUser) {
                statement = connection.prepareStatement("SELECT name FROM events WHERE status !='DEVELOPING' AND name LIKE ?");
            } else {
                statement = connection.prepareStatement("SELECT name FROM events WHERE name LIKE ?");
            }
            statement.setString(1, prefix);
            rs = statement.executeQuery();
            while (rs.next()) {
                headers.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            throw new DaoException("Cannot get Event Headers", e);
        } finally {
            closeResources(rs, statement);
            connectionManager.close(connection);
        }
        return headers;
    }

    @Override
    public boolean joinEvent(long eventId, long userId) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = connectionManager.getConnection();
            statement = connection.prepareStatement("INSERT INTO participants (event_id, user_id) VALUES (?, ?)");
            statement.setLong(1, eventId);
            statement.setLong(2, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Cannot join to event", e);
        } finally {
            closeResources(null, statement);
            connectionManager.close(connection);
        }
    }

    @Override
    public boolean leaveEvent(long eventId, long userId) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = connectionManager.getConnection();
            statement = connection.prepareStatement("DELETE FROM participants WHERE event_id=? AND user_id=?");
            statement.setLong(1, eventId);
            statement.setLong(2, userId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException("Cannot leave event", e);
        } finally {
            closeResources(null, statement);
            connectionManager.close(connection);
        }
    }

    @Override
    public boolean isJoined(long eventId, long userId) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionManager.getConnection();
            statement = connection.prepareStatement("SELECT * FROM participants WHERE event_id=? AND user_id=?");
            statement.setLong(1, eventId);
            statement.setLong(2, userId);
            resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (SQLException e) {
            throw new DaoException("Cannot execute method isJoined()", e);
        } finally {
            closeResources(resultSet, statement);
            connectionManager.close(connection);
        }
    }

    @Override
    protected Event getEntity(ResultSet rs) throws SQLException {
        Event event = new Event();
        event.setId(rs.getLong("id"));
        event.setName(rs.getString("name"));
        event.setDescription(rs.getString("description"));
        event.setStartTime(rs.getTimestamp("start_time"));
        event.setEndTime(rs.getTimestamp("end_time"));
        event.setPlace(rs.getString("place"));
        event.setLimit(rs.getInt("reports_limit"));
        event.setStatus(Event.Status.valueOf(rs.getString("status")));
        return event;
    }

    @Override
    protected void sendEntity(PreparedStatement statement, Event event) throws SQLException {
        statement.setString(1, event.getName());
        if (event.getDescription() == null) {
            statement.setNull(2, Types.NULL);
        } else {
            statement.setString(2, event.getDescription());
        }
        statement.setTimestamp(3, event.getStartTime());
        statement.setTimestamp(4, event.getEndTime());
        statement.setString(5, event.getPlace());
        statement.setInt(6, event.getLimit());
        statement.setString(7, event.getStatus().name());
    }

    private MySQLQueryBuilder eventsQueryBuilder(Map<String, SortParameter.Order> sorters, Map<String, String> filters) throws DaoException {
        List<FilterParameter> preparedFilters = mapFilter(filters);
        List<SortParameter> preparedSorters = mapSorter(sorters);

        MySQLQueryBuilder subQuery = new MySQLQueryBuilder()
                .select().count("id")
                .as("count")
                .addSelectField()
                .setValues("stub")
                .as("stub")
                .from("events")
                .where(preparedFilters);
        return new MySQLQueryBuilder()
                .select().all()
                .from("events")
                .leftJoin(subQuery)
                .setField("results")
                .on("stub", "stub")
                .where(preparedFilters)
                .orderBy(preparedSorters);
    }

    private List<SortParameter> mapSorter(Map<String, SortParameter.Order> rawSorters) throws DaoException {
        List<SortParameter> sorters = new LinkedList<>();
        if (rawSorters.isEmpty()) {
            sorters.add(new SortParameter("start_time", SortParameter.Order.DESC));
            return sorters;
        }
        if (rawSorters.containsKey("time")) {
            sorters.add(new SortParameter("start_time", rawSorters.get("time")));
        }
        if (rawSorters.containsKey("reports")) {
            MySQLQueryBuilder qb = new MySQLQueryBuilder()
                    .select().count()
                    .from("reports")
                    .where().setField("events.id").setCondition(EQUAL).setField("reports.event_id");
            sorters.add(new SortParameter(qb, rawSorters.get("reports")));
        }
        if (rawSorters.containsKey("participants")) {
            MySQLQueryBuilder qb = new MySQLQueryBuilder()
                    .select().count()
                    .from("participants")
                    .where().setField("events.id").setCondition(EQUAL).setField("participants.event_id");
            sorters.add(new SortParameter(qb, rawSorters.get("participants")));
        }
        return sorters;
    }

    private List<FilterParameter> mapFilter(Map<String, String> rawFilters) throws DaoException {
        List<FilterParameter> filters = new LinkedList<>();
        FilterFactory factory = new FilterFactory();
        for (Map.Entry<String, String> entry : rawFilters.entrySet()) {
            filters.add(factory.getFilter(entry.getKey(), entry.getValue()));
        }
        return filters;
    }
}
