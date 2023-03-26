package com.Util;

import com.dao.UserDaoJDBCTemplate;
import com.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;


@Component
public class UserValidator implements Validator {

    private final UserDaoJDBCTemplate userDaoJDBCTemplate;

    @Autowired
    public UserValidator(UserDaoJDBCTemplate userDaoJDBCTemplate) {
        this.userDaoJDBCTemplate = userDaoJDBCTemplate;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;
        if (userDaoJDBCTemplate.getUserByEmail(user.getEmail()).isPresent()) {
            errors.rejectValue("email",
                    "Duplicate.userForm.email",
                    "Пользователь с таким email уже существует");
        }
    }


}
