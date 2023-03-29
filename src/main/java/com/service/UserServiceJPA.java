package com.service;


import com.models.User;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

//Класс UserServiceJPA реализует интерфейс UserService и использует репозиторий UserRepository для работы с базой данных.
@Service
@Transactional(readOnly = true)
public class UserServiceJPA implements UserService {

    private final UserRepository userRepository;


    @Autowired
    public UserServiceJPA(UserRepository userRepository) {
        this.userRepository = userRepository;
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


    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }


}
