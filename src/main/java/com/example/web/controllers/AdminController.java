package com.example.web.controllers;

import com.example.web.models.User;
import com.example.web.models.enums.Role;
import com.example.web.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Map;

/**
 * Контроллер, отвечающий за функционал администратора
 */
@Controller
@RequiredArgsConstructor
@PreAuthorize("hasAuthority('ROLE_ADMIN')") // Аннотация для проверки доступа к контроллеру только у пользователей с ролью ROLE_ADMIN
public class AdminController {
    private final UserService userService; // Сервис для работы с пользователями

    /**
     * Обработчик GET запроса по адресу /admin
     * Отображает страницу администратора со списком пользователей и информацией об аутентифицированном пользователе
     */
    @GetMapping("/admin")
    public String admin(Model model, Principal principal) {
        model.addAttribute("users", userService.list()); // Добавление списка пользователей в модель
        model.addAttribute("user", userService.getUserByPrincipal(principal)); // Добавление информации об аутентифицированном пользователе в модель
        return "admin"; // Возвращает имя представления admin
    }

    /**
     * Обработчик POST запроса по адресу /admin/user/ban/{id}
     * Запрещает доступ пользователю с заданным идентификатором
     */
    @PostMapping("/admin/user/ban/{id}")
    public String userBan(@PathVariable("id") Long id) {
        userService.banUser(id); // Вызов метода сервиса для запрета доступа пользователю
        return "redirect:/admin"; // Перенаправление на страницу администратора
    }

    /**
     * Обработчик GET запроса по адресу /admin/user/edit/{user}
     * Отображает страницу редактирования пользователя
     */
    @GetMapping("/admin/user/edit/{user}")
    public String userEdit(@PathVariable("user") User user, Model model, Principal principal) {
        model.addAttribute("user", user); // Добавление пользователя в модель
        model.addAttribute("user", userService.getUserByPrincipal(principal)); // Добавление информации об аутентифицированном пользователе в модель
        model.addAttribute("roles", Role.values()); // Добавление ролей в модель
        return "user-edit"; // Возвращает имя представления user-edit
    }

    /**
     * Обработчик POST запроса по адресу /admin/user/edit
     * Изменяет роли пользователя на основе переданных параметров формы
     */
    @PostMapping("/admin/user/edit")
    public String userEdit(@RequestParam("userId") User user, @RequestParam Map<String, String> form) {
        userService.changeUserRoles(user, form); // Вызов метода сервиса для изменения ролей пользователя
        return "redirect:/admin"; // Перенаправление на страницу администратора
    }
}
