package com.webapp.conferences.dao.impl;

import com.webapp.conferences.dao.DAOException;
import com.webapp.conferences.dao.UserDao;
import com.webapp.conferences.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDaoTest implements UserDao {

    private final List<User> users = new ArrayList<>();


    public Optional<User> getUserByLogin(String login) {
        for(User user: users) {
            if(user.getLogin().equals(login)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<User> findAll() throws DAOException {
        return null;
    }

    @Override
    public Optional<User> findById(long id) throws DAOException {
        return Optional.empty();
    }

    @Override
    public void add(User user) throws DAOException {

    }

    @Override
    public void update(User user) throws DAOException {

    }

    @Override
    public void delete(long userId) throws DAOException {

    }

    public void addUser(long id, String login, String password, User.ROLE role) {
        users.add(new User(id, login, password, role));

    }

    public boolean addUser(User user) {
        if(isExistsUser(user.getLogin())) {
            return false;
        }

        return users.add(user);
    }

    @Override
    public boolean addUser(String login, String password) {
        if(isExistsUser(login)) {
            return false;
        }
        users.add(new User(0, login, password, User.ROLE.USER));
        return false;
    }

    @Override
    public boolean isExistsUser(String login) {
        for (User u: users) {
            if(u.getLogin().equals(login)) {
                return true;
            }
        }
        return false;
    }

}
