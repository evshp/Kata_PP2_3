package com.dao;

import com.models.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class UserDaoHibernate implements UserDao{
    @Override
    public void createUsersTable() throws SQLException {


    }

    @Override
    public void dropUsersTable() throws SQLException {

    }

    @Override
    public void saveUser(String name, String lastName, LocalDate dateOfBirth, byte age, String email) {

    }

    @Override
    public void removeUserById(long id) {

    }

    @Override
    public User getUserById(long id) {
        return null;
    }

    @Override
    public List<User> getAllUsers() {
        return null;
    }

    @Override
    public void cleanUsersTable() {

    }

    @Override
    public void update(long id, User user) {

    }
}
