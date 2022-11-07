package com.webapp.conferences.dao.impl.mysql;

import com.webapp.conferences.dao.ConnectionManager;
import com.webapp.conferences.dao.GenericDao;
import com.webapp.conferences.dao.ReportDao;
import com.webapp.conferences.exceptions.DaoException;
import com.webapp.conferences.model.Report;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class MySQLReportDao extends GenericDao<Report> implements ReportDao {

    private final Logger logger = LogManager.getLogger("Global");

    protected MySQLReportDao() {
        super(MySQLConnectionManager.getInstance());
        logger.trace("MySQl ReportDao created");
    }

    public MySQLReportDao(ConnectionManager connectionManager) {
        super(connectionManager);
        logger.trace("MySQl ReportDao created");
    }

    @Override
    public List<Report> findAll() throws DaoException {
        logger.trace("Finding all reports");
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            return findAll(connection, "SELECT * FROM reports");
        } finally {
            connectionManager.close(connection);
        }
    }

    @Override
    public List<Report> findByEvent(long eventId) throws DaoException {
        logger.debug("Find reports by Event");
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            return findByFields(connection, "SELECT * FROM reports WHERE event_id = ? AND status != 'PROPOSED'", eventId);
        } finally {
            connectionManager.close(connection);
        }
    }

    @Override
    public List<Report> findProposedByEvent(long eventId) throws DaoException {
        logger.debug("Find reports by Event");
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            return findByFields(connection, "SELECT * FROM reports WHERE event_id = ? AND status = 'PROPOSED'", eventId);
        } finally {
            connectionManager.close(connection);
        }
    }

    @Override
    public Optional<Report> findById(long id) throws DaoException {
        logger.trace("Finding reports by id");
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            List<Report> result = findByFields(connection, "SELECT * FROM reports WHERE id=?", id);
            return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
        } finally {
            connectionManager.close(connection);
        }
    }

    @Override
    public Report add(Report report) throws DaoException {
        logger.trace("Adding report");
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            long genId = add(connection, "INSERT INTO reports(event_id, speaker_id, topic, status) VALUES(?, ?, ?, ?)", report);
            report.setId(genId);
            return report;
        } finally {
            connectionManager.close(connection);
        }
    }

    @Override
    public boolean update(Report report) throws DaoException {
        logger.trace("Updating report");
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            return updateByField(connection,
                    "UPDATE reports SET event_id=?, speaker_id=?, topic=?, status=? WHERE id=?",
                    report, report.getId(), 5);
        } finally {
            connectionManager.close(connection);
        }
    }

    @Override
    public boolean delete(long reportId) throws DaoException {
        logger.trace("Deleting report");
        Connection connection = null;
        try {
            connection = connectionManager.getConnection();
            return deleteByField(connection, "DELETE FROM reports WHERE id=?", reportId);
        } finally {
            connectionManager.close(connection);
        }
    }

    @Override
    public boolean addAll(List<Report> reports) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = connectionManager.getTransaction();
            statement = connection.prepareStatement("INSERT INTO reports(event_id, speaker_id, topic, status) VALUES(?, ?, ?, ?)");
            for (Report report : reports) {
                sendEntity(statement, report);
                statement.addBatch();
            }
            statement.executeBatch();
            connection.commit();
            return true;
        } catch (SQLException e) {
            connectionManager.rollback(connection);
            throw new DaoException("Reports not inserted", e);
        } finally {
            closeResources(null, statement);
            connectionManager.close(connection);
        }
    }

    @Override
    public boolean createReportRequest(long reportId, long speakerId) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = connectionManager.getConnection();
            statement = connection.prepareStatement("INSERT  INTO reportrequest values (?, ?)");
            statement.setLong(1, reportId);
            statement.setLong(2, speakerId);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DaoException(e);
        } finally {
            closeResources(null, statement);
            connectionManager.close(connection);
        }
    }

    @Override
    public boolean acceptSpeaker(long reportId, long speakerId) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = connectionManager.getTransaction();
            updateByField(connection, "UPDATE reports SET speaker_id=?, status=? WHERE id=?",
                    speakerId, Report.Status.CONSOLIDATED.name(), reportId);
            statement = connection.prepareStatement("DELETE  FROM reportrequest WHERE report_id = ? AND  speaker_id = ?");
            statement.setLong(1, reportId);
            statement.setLong(2, speakerId);
            statement.executeUpdate();
            connectionManager.commit(connection);
            return true;
        } catch (DaoException | SQLException e) {
            connectionManager.rollback(connection);
            throw new DaoException(e);
        } finally {
            closeResources(null, statement);
            connectionManager.close(connection);
        }
    }

    @Override
    public boolean rejectSpeaker(long reportId, long speakerId) throws DaoException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = connectionManager.getConnection();
            statement = connection.prepareStatement("DELETE  FROM reportrequest WHERE report_id = ? AND  speaker_id = ?");
            statement.setLong(1, reportId);
            statement.setLong(2, speakerId);
            statement.executeUpdate();
            return true;
        } catch (DaoException | SQLException e) {
            connectionManager.rollback(connection);
            throw new DaoException(e);
        } finally {
            closeResources(null, statement);
            connectionManager.close(connection);
        }
    }

    @Override
    protected Report getEntity(ResultSet rs) throws SQLException {
        Report report = new Report();
        report.setId(rs.getLong("id"));
        report.setEventId(rs.getLong("event_id"));
        report.setSpeakerId(rs.getLong("speaker_id"));
        report.setTopic(rs.getString("topic"));
        report.setStatus(Report.Status.valueOf(rs.getString("status")));
        return report;
    }

    @Override
    protected void sendEntity(PreparedStatement statement, Report report) throws SQLException {
        statement.setLong(1, report.getEventId());
        if (report.getSpeakerId() < 1) {
            statement.setNull(2, Types.NULL);
        } else {
            statement.setLong(2, report.getSpeakerId());
        }
        statement.setString(3, report.getTopic());
        statement.setString(4, report.getStatus().name());
    }

}
