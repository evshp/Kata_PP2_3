package com.dao;

import com.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.Optional;


//Класс для работы с базой данных
@Component
public class UserDaoJDBCTemplate {


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
                 "email VARCHAR(50) NOT NULL UNIQUE, \n" +
                ");";
        jdbcTemplate.execute(tableCreate);
    }

    // Метод для удаления таблицы
    public void dropUsersTable() {
        String dropTable = "DROP TABLE IF EXISTS users;";
        jdbcTemplate.execute(dropTable);
    }


    public Optional<User> getUserByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM users WHERE email = ?"
                        , new BeanPropertyRowMapper<>(User.class), email) // BeanPropertyRowMapper - преобразует строку в объект
                .stream().findAny();
    }


}

