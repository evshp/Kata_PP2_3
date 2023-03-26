package com.service;

import com.models.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface UserService {
    void createUsersTable() throws SQLException;

    void dropUsersTable() throws SQLException;

    void saveUser(String name, String lastName, LocalDate dateOfBirth , byte age, String email);

    void removeUserById(long id);

    User getUserById(long id);

    List<User> getAllUsers();

    void cleanUsersTable();

    void update(long id, User user);
}
