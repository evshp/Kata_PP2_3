package com.dao;

import org.springframework.jdbc.core.RowMapper;
import com.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;

// Класс для маппинга данных из базы данных в объект User
public class UserMapper implements RowMapper <User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setLastname(rs.getString("lastName"));
        user.setDateOfBirth(rs.getDate("dateOfBirth").toLocalDate());
        user.setAge(rs.getByte("age"));
        user.setEmail(rs.getString("email"));
        return user;
    }
}
// Вместо этого: можно использовать в DAO классе new BeanPropertyRowMapper<>(User.class)
// вместо new UserMapper() и не создавать класс UserMapper, будет работать только если
// поля в классе User и в таблице БД совпадают по имени и типу данных