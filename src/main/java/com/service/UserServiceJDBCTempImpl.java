package com.service;

import com.dao.UserDaoJDBCTemplate;
import com.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;


@Component
public class UserServiceJDBCTempImpl implements UserService{

    private final UserDaoJDBCTemplate userDaoJDBCTemplate;

    @Autowired
    public UserServiceJDBCTempImpl(UserDaoJDBCTemplate userDaoJDBCTemplate) {
        this.userDaoJDBCTemplate = userDaoJDBCTemplate;
    }

    @Override
    public void createUsersTable() throws SQLException {
        userDaoJDBCTemplate.createUsersTable();
    }

    @Override
    public void dropUsersTable() throws SQLException {
        userDaoJDBCTemplate.dropUsersTable();
    }

    @Override
    public void saveUser(String name, String lastName, LocalDate dateOfBirth, byte age, String email) {
        userDaoJDBCTemplate.saveUser(name, lastName, dateOfBirth, age, email);
    }

    @Override
    public void removeUserById(long id) {
        userDaoJDBCTemplate.removeUserById(id);
    }

    @Override
    public User getUserById(long id) {
        return userDaoJDBCTemplate.getUserById(id);

    }

    @Override
    public List<User> getAllUsers() {
        return userDaoJDBCTemplate.getAllUsers();
    }

    @Override
    public void cleanUsersTable() {
        userDaoJDBCTemplate.cleanUsersTable();
    }

    @Override
    public void update(long id, User user) {
        userDaoJDBCTemplate.update(id, user);
    }
}
