package com.webapp.conferences.services;

import com.webapp.conferences.dao.UserDao;
import com.webapp.conferences.exceptions.DaoException;
import com.webapp.conferences.model.User;


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

    public UserService(UserDao userDao) throws DaoException {
        this.userDao = userDao;
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
        user.setRole(User.ROLE.USER);
        user.setId(userDao.add(user));
        return user;
    }

    /**
     * Returns {@code true} if user with specified login is exists
     * and specified password hash is equals.
     * @param login - element represent login from view layer
     * @param password - clear text password from view layer
     * @return {@code true} if user with specified parameters is exists
     */
    public boolean validate(String login, String password) throws DaoException {
        if(!(nonNull(login) && nonNull(password))) {
            return false;
        }

        Optional<User> optional = userDao.getUserByLogin(login);
        return optional.map(user -> checkpw(password, user.getPassword())).orElse(false);
    }

    public boolean addUser(User user) throws DaoException {
        return userDao.add(user) > 0;
    }

    public Optional<User> getUser(String login) throws DaoException {
        return userDao.getUserByLogin(login);
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
