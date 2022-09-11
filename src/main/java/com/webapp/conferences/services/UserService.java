package com.webapp.conferences.services;

import com.webapp.conferences.dao.UserDao;
import com.webapp.conferences.dao.impl.UserDaoTest;
import com.webapp.conferences.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

import static java.util.Objects.nonNull;

public class UserService {
    private final UserDao userDao = new UserDaoTest();
    private static UserService instance;

    private UserService() {

    }
    public static synchronized UserService getInstance() {
        if(instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public boolean validation(String login, String password) {
        if(!(nonNull(login) && nonNull(password))) {
            return false;
        }

        Optional<User> optional = userDao.getUserByLogin(login);
        return optional.map(user -> BCrypt.checkpw(password, user.getPassword())).orElse(false);
    }

    public boolean addUser(String login, String password, User.ROLE role) {
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        return userDao.addUser(new User(0, login, hash, role));
    }

    public Optional<User> getUser(String login) {
        return  userDao.getUserByLogin(login);
    }

}
