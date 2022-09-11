package com.webapp.conferences.dao.impl;

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

    public void addUser(long id, String login, String password, User.ROLE role) {
        users.add(new User(id, login, password, role));

    }

    public boolean addUser(User user) {
        for (User u: users) {
            if(u.getLogin().equals(user.getLogin())) {
                return false;
            }
        }
        users.add(user);
        return true;
    }

}
