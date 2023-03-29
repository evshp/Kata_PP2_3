package com.controllers;

import com.Util.UserValidator;
import com.models.User;


import com.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class UsersController {


    private UserService userServiceJPA;
    private UserValidator userValidator;

    @Autowired
    public UsersController(UserService userServiceJPA, UserValidator userValidator) {
        this.userServiceJPA = userServiceJPA;
        this.userValidator = userValidator;
    }

    //Показать всех пользователей
    @GetMapping("/people")
    public String index(Model model) {
        try {
            model.addAttribute("users", userServiceJPA.getAllUsers());
        } catch (Exception e) {
            System.out.println("Ошибка: " + e);
        } finally {
            model.addAttribute("formUser", new User());
        }
        return "users/userPage";
    }

    //Получить пользователя по id
    @GetMapping("userPage/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("users", userServiceJPA.getAllUsers());
        model.addAttribute("user", userServiceJPA.getUserById(id));
        model.addAttribute("formUser", new User());
        return "users/userPage";
    }

    //Создать нового пользователя
    @PostMapping("/postAction")
    public String create(@ModelAttribute("formUser") @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "users/userPage";
        }
        try {
            userServiceJPA.saveUser(user.getName(),
                    user.getLastname(),
                    user.getDateOfBirth(),
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
        model.addAttribute("user", userServiceJPA.getUserById(id));
        return "users/edit";
    }

    //Метод для обновления данных пользователя
    @PatchMapping("/users/{id}")
    public String update(@ModelAttribute("user") @Valid User user,
                         BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "users/edit";
        }
        userServiceJPA.update(user.getId(), user);
        return "redirect:/people";
    }


    //Метод для удаления пользователя
    @DeleteMapping("/delete")
    public String delete(@ModelAttribute("user") User user) {
        userServiceJPA.removeUserById(user.getId());
        System.out.println("Удален пользователь с id: " + user.getId());
        return "redirect:/people";
    }


}
