package com.dao;

import com.Util.Util;
import com.models.User;

import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


//Класс для работы с базой данных
@Repository
public class UserDaoJDBC implements UserDao {

    private final static Connection connection = Util.getConnection();

    public UserDaoJDBC() {
    }

    // Метод для создания новой таблицы
    public void createUsersTable() {
        String tableCreate = "CREATE TABLE IF NOT EXISTS users (\n" +
                "id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,\n" +
                "name VARCHAR(50) NOT NULL,\n" +
                "lastName VARCHAR(50) NOT NULL,\n" +
                "dateOfBirth DATE NOT NULL,\n" +
                "age INT NOT NULL \n" +
                ");";

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(tableCreate);
            System.out.println("Таблица users создана");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println("Ошибка отката транзакции: " + ex.getMessage());
            }
            throw new RuntimeException(e);
        }

    }

    // Метод для удаления таблицы
    public void dropUsersTable() {
        String dropTable = "DROP TABLE IF EXISTS users;";
        try (Statement statement = connection.createStatement()) {
            statement.execute(dropTable);
            System.out.println("Таблица удаленна");

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println("Ошибка отката транзакции: " + ex.getMessage());
            }
            throw new RuntimeException(e);
        }

    }

    // Метод для сохранения пользователя
    public void saveUser(String name, String lastName, LocalDate dateOfBirth, byte age, String email) {
        String saveUser = "INSERT INTO users (name, lastname, dateOfBirth, age) " +
                "VALUES (?, ?, ?, ?)";
        if (name.length() <= 50 & lastName.length() <= 50) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(saveUser)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, lastName);
                preparedStatement.setDate(3, java.sql.Date.valueOf(dateOfBirth));
                preparedStatement.setInt(4, age);
                preparedStatement.executeUpdate();
                System.out.println("User c именем - " + name + " добавлен в базу данных");
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    System.out.println("Ошибка отката транзакции: " + ex.getMessage());
                }
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Длина имени и фамилии не может превышать 50 символов");
        }

    }

    // Метод для удаления пользователя по id
    public void removeUserById(long id) {
        String removeUserById = "DELETE FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(removeUserById)) {
            preparedStatement.setLong(1, id);
            int count = preparedStatement.executeUpdate();
            System.out.println("Строка с id =" + id + " была удаленна.\n" +
                    "Всего измененых строк " + count);
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println("Ошибка отката транзакции: " + ex.getMessage());
            }
            throw new RuntimeException(e);
        }

    }

    // Метод для получения пользователя по id
    @Override
    public User getUserById(long id) {
        String getUserById = "SELECT * FROM users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(getUserById)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                long userId = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String lastName = resultSet.getString("lastName");
                LocalDate dayOfBirth = resultSet.getDate("dateOfBirth").toLocalDate();
                byte age = resultSet.getByte("age");
                return new User(userId, name, lastName, dayOfBirth, age);
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println("Ошибка отката транзакции: " + ex.getMessage());
            }
            throw new RuntimeException(e);

        }
        return null;
    }

    // Метод для получения всех пользователей из таблицы
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String getAllUsers = "SELECT * FROM users";
        System.out.println((connection == null) ? "Connection is null" : "Connection is not null");
        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(getAllUsers)) {
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                String lastname = resultSet.getString("lastName");
                byte age = resultSet.getByte("age");
                LocalDate dateOfBirth = resultSet.getDate("dateOfBirth").toLocalDate();
                String email = resultSet.getString("email");
                users.add(new User(id, name, lastname, dateOfBirth, age, email));
                System.out.println("Пользователь с id = " + id + " получен " + name + " " + lastname);
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println("Ошибка отката транзакции: " + ex.getMessage());
            }
            throw new RuntimeException(e);

        }
        System.out.println("Пользователи получены");
        System.out.println(users);

        return users;
    }

    // Метод для очистки таблицы
    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE users;");
            System.out.println("Таблица очищена");

        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println("Ошибка отката транзакции: " + ex.getMessage());
            }
            throw new RuntimeException(e);

        }

    }

    // Метод для обновления данных в таблице
    @Override
    public void update(long id, User user) {
        String update = "UPDATE users SET name = ?, lastName = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(update)) {
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getLastname());
            preparedStatement.setLong(3, id);
            preparedStatement.executeUpdate();
            System.out.println("User c именем - " + user.getName() + " обновлен");
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                System.out.println("Ошибка отката транзакции: " + ex.getMessage());
            }
            throw new RuntimeException(e);
        }
    }

}

