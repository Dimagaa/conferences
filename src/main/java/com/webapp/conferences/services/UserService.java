package com.webapp.conferences.services;

import com.webapp.conferences.dao.DaoFactory;
import com.webapp.conferences.dao.UserDao;
import com.webapp.conferences.exceptions.DaoException;
import com.webapp.conferences.model.User;


import java.util.Optional;

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

    public UserService(String db) throws DaoException {
        this.userDao = DaoFactory.getDaoFactory(db).getUserDao();
    }

    /**
     * Returns {@code true} if user with specified login is exists
     * and specified password hash is equals.
     * @param login - element represent login from view layer
     * @param password - clear text password from view layer
     * @return {@code true} if user with specified parameters is exists
     */
    public boolean validation(String login, String password) throws DaoException {
        if(!(nonNull(login) && nonNull(password))) {
            return false;
        }

        Optional<User> optional = userDao.getUserByLogin(login.toLowerCase());
        return optional.map(user -> checkpw(password, user.getPassword())).orElse(false);
    }

    public boolean addUser(String login, String password, String firstN, String lastN, User.ROLE role) throws DaoException {
        User user = new User();
        user.setLogin(login);
        user.setPassword(hashpw(password, gensalt()));
        user.setFirstName(firstN);
        user.setLastName(lastN);
        user.setRole(role);
        return userDao.add(user) > 0;
    }

    public Optional<User> getUser(String login) throws DaoException {
        return userDao.getUserByLogin(login);
    }


}
