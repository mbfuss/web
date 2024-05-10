package com.example.web.controllers;

import com.example.web.models.User;
import com.example.web.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

/**
 * Контроллер, отвечающий за обработку запросов, связанных с пользователями
 */
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Обработка GET запроса для страницы входа
     *  principal объект, представляющий текущего аутентифицированного пользователя
     *  model объект для передачи данных в представление
     *  return имя представления "login"
     */
    @GetMapping("/login")
    public String login(Principal principal, Model model) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "login";
    }

    /**
     * Обработка GET запроса для страницы профиля
     *  principal объект, представляющий текущего аутентифицированного пользователя
     *  model объект для передачи данных в представление
     *  return имя представления "profile"
     */
    @GetMapping("/profile")
    public String profile(Principal principal, Model model) {
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        return "profile";
    }

    /**
     * Обработка GET запроса для страницы регистрации
     *  principal объект, представляющий текущего аутентифицированного пользователя
     *  model объект для передачи данных в представление
     *   имя представления "registration"
     */
    @GetMapping("/registration")
    public String registration(Principal principal, Model model) {
        model.addAttribute("user", userService.getUserByPrincipal(principal));
        return "registration";
    }

    /**
     * Обработка POST запроса для создания пользователя
     *   объект пользователя, переданный из формы
     *   model объект для передачи данных в представление
     * return перенаправление на страницу входа, если пользователь успешно создан, иначе возвращается страница регистрации с сообщением об ошибке
     */
    @PostMapping("/registration")
    public String createUser(User user, Model model) {
        if (!userService.createUser(user)) {
            model.addAttribute("errorMessage", "Пользователь с email: " + user.getEmail() + " уже существует");
            return "registration";
        }
        return "redirect:/login";
    }

    /**
     * Обработка GET запроса для отображения информации о пользователе
     * user объект пользователя, полученный из пути URI
     *  model объект для передачи данных в представление
     *  principal объект, представляющий текущего аутентифицированного пользователя
     *   имя представления "user-info" с информацией о пользователе и его продуктах
     */
    @GetMapping("/user/{user}")
    public String userInfo(@PathVariable("user") User user, Model model, Principal principal) {
        model.addAttribute("user", user);
        model.addAttribute("userByPrincipal", userService.getUserByPrincipal(principal));
        model.addAttribute("products", user.getProducts());
        return "user-info";
    }
}
