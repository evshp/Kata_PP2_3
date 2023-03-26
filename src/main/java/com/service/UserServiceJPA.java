package com.service;


import com.dao.UserDaoJDBCTemplate;
import com.models.User;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional (readOnly = true)
public class UserServiceJPA implements UserService {

    private final UserRepository userRepository;
    private final UserDaoJDBCTemplate userDaoJDBCTemplate;

    @Autowired
    public UserServiceJPA(UserRepository userRepository, UserDaoJDBCTemplate userDaoJDBCTemplate) {
        this.userDaoJDBCTemplate = userDaoJDBCTemplate;
        this.userRepository = userRepository;
    }


    @Override
    public void createUsersTable() throws SQLException {
        userDaoJDBCTemplate.createUsersTable();
    }

    @Override
    public void dropUsersTable() throws SQLException {
        userDaoJDBCTemplate.dropUsersTable();
    }


    @Transactional
    @Override
    public void saveUser(String name, String lastName, LocalDate dateOfBirth, String email) {
        userRepository.save(new User(name, lastName, dateOfBirth, email));
    }

    @Transactional
    @Override
    public void removeUserById(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUserById(long id) {
        Optional<User> foundUser = userRepository.findById(id);
        return foundUser.orElse(null);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    @Transactional
    @Override
    public void cleanUsersTable() {
        userRepository.deleteAll();
    }


    @Transactional
    @Override
    public void update(long id, User user) {
        User userFromDB = getUserById(id);
        userFromDB.setName(user.getName());
        userFromDB.setLastname(user.getLastname());
        userFromDB.setEmail(user.getEmail());
        userRepository.save(userFromDB);
    }





}
