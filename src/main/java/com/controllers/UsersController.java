package com.controllers;

import com.Util.UserValidator;
import com.dao.UserDaoJDBCTemplate;
import com.models.User;

import com.service.UserServiceJDBCTempImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsersController {


    private UserServiceJDBCTempImpl userServiceJDBCTemp;
    private UserValidator userValidator;

    @Autowired
    public UsersController(UserServiceJDBCTempImpl userServiceJDBCTemp, UserValidator userValidator) {
        this.userServiceJDBCTemp = userServiceJDBCTemp;
        this.userValidator = userValidator;
    }

    //Показать всех пользователей
    @GetMapping("/people")
    public String index(Model model) {

        try {
            model.addAttribute("users", userServiceJDBCTemp.getAllUsers());
            model.addAttribute("user", new User());
        } catch (Exception e) {
            System.out.println("Ошибка: " + e);
        } finally {
            model.addAttribute("formUser", new User());
        }

        return "users/userPage";
    }

    //Получить пользователя по id
    @GetMapping("userPage/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("formUser") User user) {
        model.addAttribute("users", userServiceJDBCTemp.getAllUsers());
        model.addAttribute("user", userServiceJDBCTemp.getUserById(id));
        return "users/userPage";
    }

    //Создать нового пользователя
    @PostMapping("/postAction")
    public String create(@ModelAttribute("formUser") @Valid User user, BindingResult bindingResult, Model model) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasFieldErrors("name") ||
                bindingResult.hasFieldErrors("lastName")) {
            model.addAttribute("user", userServiceJDBCTemp.getUserById(0));
            return "users/userPage";
        }
        try {
            userServiceJDBCTemp.saveUser(user.getName(),
                    user.getLastname(),
                    user.getDateOfBirth(),
                    user.getAge(),
                    user.getEmail());
        } catch (Exception e) {
            System.out.println("Исключение: " + e.getMessage());
            return "error";
        }
        return "redirect:/people";
    }

    //Получаю id пользователя из URL. Как получить из страницы
    @PostMapping("/edit")
    public String edit(Model model, @RequestParam("id") long id) {
        model.addAttribute("user", userServiceJDBCTemp.getUserById(id));
        return "users/edit";
    }

    //Метод для обновления данных пользователя
    @PatchMapping("/users/{id}")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult) {

        if (bindingResult.hasFieldErrors("name") ||
                bindingResult.hasFieldErrors("lastname")) {
            return "users/edit";
        }

        userServiceJDBCTemp.update(user.getId(), user);
        return "redirect:/people";
    }


    //Метод для удаления пользователя
    @DeleteMapping("/delete")
    public String delete(@ModelAttribute("user") User user) {
        userServiceJDBCTemp.removeUserById(user.getId());
        return "redirect:/people";
    }


}
