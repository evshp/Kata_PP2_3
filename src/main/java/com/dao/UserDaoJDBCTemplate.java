package com.dao;

import com.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


//Класс для работы с базой данных
@Repository
public class UserDaoJDBCTemplate implements UserDao {


    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDaoJDBCTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    // Метод для создания новой таблицы
    public void createUsersTable() {
        String tableCreate = "CREATE TABLE IF NOT EXISTS users (\n" +
                "id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,\n" +
                "name VARCHAR(50) NOT NULL,\n" +
                "lastName VARCHAR(50) NOT NULL,\n" +
                "dateOfBirth DATE NOT NULL,\n" +
                "age INT NOT NULL CHECK (age > 0) \n" +
                "email VARCHAR(50) NOT NULL UNIQUE, \n" +
                ");";
        jdbcTemplate.execute(tableCreate);
    }

    // Метод для удаления таблицы
    public void dropUsersTable() {
        String dropTable = "DROP TABLE IF EXISTS users;";
        jdbcTemplate.execute(dropTable);
    }

    // Метод для сохранения пользователя
    public void saveUser(String name, String lastName, LocalDate dateOfBirth, byte age, String email) {
        String saveUser = "INSERT INTO users (name, lastname, dateOfBirth, age, email) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(saveUser, name, lastName, dateOfBirth, age, email);

    }

    // Метод для удаления пользователя по id
    public void removeUserById(long id) {
        String removeUserById = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(removeUserById, id);
    }

    // Метод для получения пользователя по id
    @Override
    public User getUserById(long id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE id = ?"
                        , new BeanPropertyRowMapper<>(User.class), id) // BeanPropertyRowMapper - преобразует строку в объект
                .stream().findAny().orElse(null);
    }

    // Метод для получения всех пользователей из таблицы
    public List<User> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM users", new UserMapper()); // UserMapper - преобразует строку в объект
    }

    // Метод для очистки таблицы
    public void cleanUsersTable() {
        jdbcTemplate.update("TRUNCATE TABLE users;");
        System.out.println("Таблица очищена");
    }

    // Метод для обновления данных в таблице
    @Override
    public void update(long id, User user) {
        String update = "UPDATE users SET name = ?, lastName = ?, email = ? WHERE id = ?";
        jdbcTemplate.update(update, user.getName(), user.getLastname(), user.getEmail(), id);
    }


    public Optional<User> getUserByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM users WHERE email = ?"
                        , new BeanPropertyRowMapper<>(User.class), email) // BeanPropertyRowMapper - преобразует строку в объект
                .stream().findAny();
    }


}

