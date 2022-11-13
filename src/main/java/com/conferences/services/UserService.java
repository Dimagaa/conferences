package com.conferences.services;

import com.conferences.dao.DaoFactory;
import com.conferences.dao.UserDao;
import com.conferences.exceptions.DaoException;
import com.conferences.model.User;


import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.nonNull;
import static org.mindrot.jbcrypt.BCrypt.*;

/**
 * The {@code UserService} class defines methods that provide access to {@code UserDao}
 * to view layer by checking the input data
 *
 * @author Dmytro Martyshchuk
 */

public class UserService {
    private final UserDao userDao;

    public UserService(String database)  {
        DaoFactory daoFactory = DaoFactory.getDaoFactory(database);
        this.userDao = daoFactory.getUserDao();
    }

    public User createUser(String email, String firstName, String lastName, String password) throws DaoException {
        if(!(isValidEmail(email) && isValidPassword(password))) {
            throw new IllegalArgumentException("email or password is invalid!");
        }
        final User user = new User();
        user.setLogin(email);
        user.setPassword(hashpw(password, gensalt()));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setRole(User.Role.USER);
        return userDao.add(user);
    }

    public Optional<User> getUser(String login) throws DaoException {
        return userDao.getUserByLogin(login);
    }

    /**
     * Returns {@code User} if user with specified login is exists
     * and specified password hash is equals or {@code null}.
     * @param login - element represent login from view layer
     * @param password - clear text password from view layer
     * @return {@code User} if user with specified parameters is exists or {@code null;}
     */
    public User authenticatedUser(String login, String password) throws DaoException {
        if(!(nonNull(login) && nonNull(password))) {
            throw new IllegalArgumentException("Cannot authenticate User, because parameters is null");
        }
        return getUser(login).filter( u -> checkpw(password, u.getPassword())).orElse(null);
    }

    public List<User> getAllSpeakers() throws DaoException {
        return userDao.findUsersByRole(User.Role.SPEAKER);
    }

    private boolean isValidEmail(String email) {
        final String regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPassword(String password) {
        final String regex = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
        final Pattern pattern = Pattern.compile(regex);
        final Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
