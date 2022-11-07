package com.webapp.conferences.model;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private long id;
    private String login;
    private String password;
    private String firstName;
    private String lastName;
    private Role role;



    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && login.equals(user.login) && password.equals(user.password) && firstName.equals(user.firstName) && lastName.equals(user.lastName) && role == user.role;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, firstName, lastName, role);
    }

    public enum Role {
        USER,
        SPEAKER,
        MODERATOR
    }
}


