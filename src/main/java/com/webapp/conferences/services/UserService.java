package com.webapp.conferences.services;

import com.webapp.conferences.dao.DaoFactory;
import com.webapp.conferences.dao.UserDao;
import com.webapp.conferences.model.User;

import java.sql.SQLException;
import java.util.List;
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
    private UserDao userDao;
    private static UserService instance;

    private UserService() {
        try {
            DaoFactory daoFactory = DaoFactory.getDaoFactory("MySql");
            userDao = daoFactory.getUserDao();
        } catch (IllegalArgumentException e) {
             e.printStackTrace();
        }
    }
    public static synchronized UserService getInstance() {
        if(instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    /**
     * Returns {@code true} if user with specified login is exists
     * and specified password hash is equals.
     * @param login - element represent login from view layer
     * @param password - clear text password from view layer
     * @return {@code true} if user with specified parameters is exists
     */
    public boolean validation(String login, String password) {
        if(!(nonNull(login) && nonNull(password))) {
            return false;
        }

        Optional<User> optional = userDao.getUserByLogin(login.toLowerCase());
        return optional.map(user -> checkpw(password, user.getPassword())).orElse(false);
    }

    public boolean addUser(String login, String password, User.ROLE role) {
        String hash = hashpw(password, gensalt());
        System.out.println(hash);
        System.out.println(hash.length());

        return userDao.addUser(new User(0, login.toLowerCase(), hash, role));
    }
    public User addUser(String login, String password) throws SQLException {
        userDao.addUser(login, hashpw(password, gensalt()));
        return userDao.getUserByLogin(login).orElseThrow(SQLException::new);
    }
    public boolean isExistsUser(String login) {
        return userDao.isExistsUser(login);
    }


    public Optional<User> getUser(String login) {
        return  userDao.getUserByLogin(login);
    }

    public List<User> findAll() throws Exception {
        return userDao.findAll();
    }
}
