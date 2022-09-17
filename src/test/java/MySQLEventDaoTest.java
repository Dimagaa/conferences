import com.webapp.conferences.dao.EventDao;
import com.webapp.conferences.dao.impl.mysql.MySQLEventDao;
import com.webapp.conferences.exceptions.DaoException;
import com.webapp.conferences.model.Event;

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

public class MySQLEventDaoTest {

    private EventDao eventDao;
    private static TestsConnectionManager connectionManager;

    private List<Event> events;

    @BeforeAll
    public static void beforeClass() {
        connectionManager = new TestsConnectionManager();
        connectionManager.init();
    }

    @BeforeEach
    public void init() {
        eventDao = new MySQLEventDao(connectionManager);
        events = creatEvents(7);
    }

    @AfterEach
    public void tearDown() throws DaoException {
        for(Event event:events) {
            eventDao.delete(event.getId());
        }
    }

    @Test
    void testFindAll() throws DaoException {
        assertTrue(eventDao.findAll().isEmpty());
        insertEvents();
        assertEquals(events, eventDao.findAll());
    }

    @Test
    void testFindById() throws DaoException {
        insertEvents();
        for(Event event : events) {
            assertEquals(event, eventDao.findById(event.getId()).orElse(null));
        }
        assertTrue(eventDao.findById(-1).isEmpty());
    }

    @Test
    void testAdd() throws DaoException {
        for(Event event : events) {
            assertTrue(eventDao.add(event) > 0);
        }
    }

    @Test
    void testUpdate() throws DaoException {
        insertEvents();
        for(Event event : events) {
            event.setPlace("Another");
            assertTrue(eventDao.update(event));
        }
        assertEquals(events, eventDao.findAll());
    }

    @Test
    void testDelete() throws DaoException {
        insertEvents();
        for(Event event : events) {
            assertTrue(eventDao.delete(event.getId()));
        }
        assertTrue(eventDao.findAll().isEmpty());
    }

    private List<Event> creatEvents(int count) {
        List<Event> res = new ArrayList<>();
        for(int i = 0; i < count; i++ ) {
            Event event = new Event();
            event.setName("testEvent"+i);
            event.setStartTime(new Timestamp(1_663_668_000_000L));
            event.setEndTime(new Timestamp(1_663_675_200_000L));
            event.setPlace("TestEvent");
            event.setLimitEvents(i+1);
            res.add(event);
        }
        return res;
    }

    private void insertEvents() throws DaoException {
        for(Event event:events) {
            eventDao.add(event);
        }
    }

}
