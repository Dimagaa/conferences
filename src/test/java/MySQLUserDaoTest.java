import com.webapp.conferences.dao.impl.mysql.MySQLUserDao;
import com.webapp.conferences.exceptions.DaoException;
import com.webapp.conferences.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TestsConnectionManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class MySQLUserDaoTest {

    private MySQLUserDao userDao;
    private static TestsConnectionManager connectorManager;

    private List<User> users;

    @BeforeAll
    public static void beforeClass() throws DaoException {
        connectorManager = new TestsConnectionManager();
        connectorManager.init();
    }
    @BeforeEach
    public void init() {
        userDao = new MySQLUserDao(connectorManager);
        users = createUserList(7);
    }

    @AfterEach
    public void tearDown() throws DaoException {
        for(User user : users) {
            userDao.delete(user.getId());
        }
    }

    @Test
    public void testFindAll() throws DaoException {
        assertTrue(userDao.findAll().isEmpty());
        insertUser();
        assertEquals(users, userDao.findAll());

    }

    @Test
    public void testAddWhenUserIsCorrect() throws DaoException {
        for (User user: users) {
            assertNotNull(userDao.add(user));
        }
    }

    @Test
    public void testFindById() throws DaoException {
        insertUser();
        for(User user : users) {
            assertEquals(user, userDao.findById(user.getId()).orElseThrow(RuntimeException::new));
        }
        assertTrue(userDao.findById(-1).isEmpty());
    }

    @Test
    public void testUpdate() throws DaoException {
        insertUser();
        for(User user : users) {
            user.setLogin(user.getLogin()+user.hashCode());
            assertTrue(userDao.update(user));
        }
        assertEquals(users, userDao.findAll());
    }

    @Test
    public void testDelete() throws DaoException {
        insertUser();
        for(User user : users) {
            assertTrue(userDao.delete(user.getId()));
        }
        assertFalse(userDao.delete(-1));

    }



    private List<User> createUserList(int count) {
        List<User> res = new ArrayList<>();
        for(int i = 0; i < count; i++) {
            User user = new User();
            user.setLogin("test_user" + i);
            user.setPassword("test_user" + i);
            user.setFirstName("test_user" + i);
            user.setLastName("test_user" + i);
            if((i+1)%3 == 0) {
                user.setRole(User.Role.USER);
            } else if((i+1)%2 == 0) {
                user.setRole(User.Role.SPEAKER);
            } else {
                user.setRole(User.Role.MODERATOR);
            }
            res.add(user);
        }
        return res;
    }

    private void insertUser() throws DaoException {
        for(User user: users) {
            userDao.add(user);
        }
    }

}
