package ServicesTest;

import com.webapp.conferences.dao.DAOException;
import com.webapp.conferences.model.User;
import com.webapp.conferences.services.UserService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class UserServiceTest {

    private UserService users = UserService.getInstance();
    private final String PASSWORD = "test";

    @Test
    public void validInsertionUserTest() {

        assertTrue(users.addUser("test10", PASSWORD, User.ROLE.USER));
        assertTrue(users.addUser("test11", PASSWORD, User.ROLE.MODERATOR));
        assertTrue(users.addUser("test12", PASSWORD, User.ROLE.SPEAKER));
    }

    @Test
    public void invalidInsertionUserTest() {
        init();
        assertFalse(users.addUser("test1", PASSWORD, User.ROLE.USER));
        assertFalse(users.addUser("test2", PASSWORD, User.ROLE.MODERATOR));
        assertFalse(users.addUser("test3", PASSWORD, User.ROLE.SPEAKER));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "alex, alex",
            "bon, bob",
            "brian, brian"
    })
    public void whenLoginAndPasswordIsCorrect(String login, String password) throws DAOException {
        users = UserService.getInstance();
        users.addUser(login, password, User.ROLE.SPEAKER);
        assertTrue(users.validation(login, password));
    }

    @ParameterizedTest
    @CsvSource(value = {
            "test1, test1",
            "test2, test2",
            "test3, brian"
    })
    public void whenLoginAndPasswordIsIncorrect(String login, String password) throws DAOException {
        init();
        assertFalse(users.validation(login, password));
    }
    @ParameterizedTest
    @CsvSource(value = {
            "test4, test1",
            "test5, test2",
            "test6, brian"
    })
    public void whenUserNotExistValidationTest(String login, String password) throws DAOException {
        init();
        assertFalse(users.validation(login, password));
    }
    @ParameterizedTest
    @NullAndEmptySource
    public void whenLoginIsEmptyAndNullValidation(String login) throws DAOException {
        init();
        assertFalse(users.validation(login, "test"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"TEST1", "TeSt2", "tEst3"})
    public void whenUseDifferentLetterCaseLogin(String login) throws DAOException {
        assertTrue(users.validation(login, PASSWORD));
    }


    private void init() {
        users.addUser("test1", PASSWORD, User.ROLE.USER);
        users.addUser("test2", PASSWORD, User.ROLE.MODERATOR);
        users.addUser("test3", PASSWORD, User.ROLE.SPEAKER);
    }
}
