import com.webapp.conferences.dao.EventDao;
import com.webapp.conferences.dao.ReportDao;
import com.webapp.conferences.dao.UserDao;
import com.webapp.conferences.dao.impl.mysql.MySQLEventDao;
import com.webapp.conferences.dao.impl.mysql.MySQLReportDao;
import com.webapp.conferences.dao.impl.mysql.MySQLUserDao;
import com.webapp.conferences.exceptions.DaoException;
import com.webapp.conferences.model.Event;
import com.webapp.conferences.model.Report;
import com.webapp.conferences.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TestsConnectionManager;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MySQLReportDaoTest {
    private ReportDao reportDao;
    private static TestsConnectionManager connectionManager;

    private List<Report> reports;
    User user;
    Event event;

    @BeforeAll
    public static void beforeClass() {
        connectionManager = new TestsConnectionManager();
        connectionManager.init();
    }

    @BeforeEach
    public void init() throws DaoException {
        reportDao = new MySQLReportDao(connectionManager);
        user = insertUser();
        event = insertEvent();
        reports = creatReports(7);
    }

    @AfterEach
    public void tearDown() throws DaoException {
        for(Report report: reports) {
            reportDao.delete(report.getId());
        }
        EventDao eventDao = new MySQLEventDao(connectionManager);
        UserDao userDao = new MySQLUserDao(connectionManager);
        eventDao.delete(event.getId());
        userDao.delete(user.getId());
    }

    @Test
    void testFindAll() throws DaoException {
        assertTrue(reportDao.findAll().isEmpty());
        insertReports();
        assertEquals(reports, reportDao.findAll());
    }

    @Test
    void testFindById() throws DaoException {
        insertReports();
        for(Report report : reports) {
            assertEquals(report, reportDao.findById(report.getId()).orElse(null));
        }
        assertTrue(reportDao.findById(-1).isEmpty());
    }

    @Test
    void testAdd() throws DaoException {
        for(Report report : reports) {
            assertTrue(reportDao.add(report) > 0);
        }
    }

    @Test
    void testUpdate() throws DaoException {
        insertReports();
        for(Report report : reports) {
            report.setTopic("Another");
            assertTrue(reportDao.update(report));
        }
        assertEquals(reports, reportDao.findAll());
    }

    @Test
    void testDelete() throws DaoException {
        insertReports();
        for(Report report : reports) {
            assertTrue(reportDao.delete(report.getId()));
        }
        assertTrue(reportDao.findAll().isEmpty());
    }

    private List<Report> creatReports(int count) {
        List<Report> res = new ArrayList<>();
        for(int i = 0; i < count; i++ ) {
            Report report = new Report();
            report.setEventId(event.getId());
            report.setSpeakerId(user.getId());
            report.setTopic("Another");
            res.add(report);
        }
        return res;
    }

    private void insertReports() throws DaoException {
        for(Report report: reports) {
            reportDao.add(report);
        }
    }

    private User insertUser() throws DaoException {
        User user = new User();
        UserDao userDao = new MySQLUserDao(connectionManager);

        user.setLogin("Test");
        user.setPassword("Test");
        user.setFirstName("Test");
        user.setLastName("Test");
        user.setRole(User.ROLE.USER);
        user.setId(userDao.add(user));

        return user;
    }

    private Event insertEvent() throws DaoException {
        Event event = new Event();
        EventDao eventDao = new MySQLEventDao(connectionManager);

        event.setName("test");
        event.setPlace("test");
        event.setStartTime(new Timestamp(System.currentTimeMillis()));
        event.setEndTime(new Timestamp(System.currentTimeMillis()));
        event.setLimitEvents(3);
        event.setId(eventDao.add(event));
        return event;
    }



}
