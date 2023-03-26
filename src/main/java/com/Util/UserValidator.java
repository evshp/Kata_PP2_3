package com.Util;

import com.dao.UserDaoJDBCTemplate;
import com.models.User;
import com.service.UserService;
import com.service.UserServiceJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class UserValidator implements Validator {

    private final UserDaoJDBCTemplate userDaoJDBCTemplate;
    private final UserService userServiceJPA;


    @Autowired
    public UserValidator(UserDaoJDBCTemplate userDaoJDBCTemplate, UserService userServiceJPA) {
        this.userDaoJDBCTemplate = userDaoJDBCTemplate;
        this.userServiceJPA = userServiceJPA;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        User userFromDB = userServiceJPA.getUserById(user.getId());
        if (userFromDB == null) {
            userFromDB = new User();
            userFromDB.setEmail("test");
        }


        if (user.getEmail() != null && !user.getEmail().equals(userFromDB.getEmail())) {
            if (userDaoJDBCTemplate.getUserByEmail(user.getEmail()).isPresent()) {
                errors.rejectValue("email",
                        "Duplicate.userForm.email",
                        "Пользователь с таким email уже существует");
            }
        }


    }


}
