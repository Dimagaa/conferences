package com.webapp.conferences.dao.impl.mysql;

import com.webapp.conferences.dao.ConnectionManager;
import com.webapp.conferences.dao.GenericDao;
import com.webapp.conferences.dao.ReportDao;
import com.webapp.conferences.exceptions.DaoException;
import com.webapp.conferences.model.Report;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        return findAll("SELECT * FROM reports");
    }

    @Override
    public Optional<Report> findById(long id) throws DaoException {
        logger.trace("Finding reports by id");
        List<Report> result = findByFields("SELECT * FROM reports WHERE id=?", id);
        return result.isEmpty() ? Optional.empty() : Optional.of(result.get(0));
    }

    @Override
    public int add(Report report) throws DaoException {
        logger.trace("Adding report");
        int genId = add("INSERT INTO reports(event_id, user_id, topic) VALUES(?, ?, ?)", report);
        report.setId(genId);
        return genId;
    }

    @Override
    public boolean update(Report report) throws DaoException {
        logger.trace("Updating report");
        return updateByField("UPDATE reports SET topic=? WHERE id=?", report, report.getId(), 2);
    }

    @Override
    public boolean delete(long reportId) throws DaoException {
        logger.trace("Deleting report");
        return deleteByField("DELETE FROM reports WHERE id=?", reportId);
    }

    @Override
    protected Report getEntity(ResultSet rs) throws SQLException {
        Report report = new Report();
        report.setId(rs.getLong("id"));
        report.setEventId(rs.getLong("event_id"));
        report.setSpeakerId(rs.getLong("speaker_id"));
        report.setTopic(rs.getString("topic"));
        return report;
    }

    @Override
    protected void sendEntity(PreparedStatement statement, Report report) throws SQLException {
        statement.setLong(1, report.getEventId());
        statement.setLong(2, report.getSpeakerId());
        statement.setString(3, report.getTopic());
    }

}
