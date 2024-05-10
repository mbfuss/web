package com.example.web.models.enums;

import org.springframework.security.core.GrantedAuthority;

/**
 * Enum, представляющий роли пользователей
 * Реализует интерфейс GrantedAuthority, который представляет собой разрешение (в данном случае - роль)
 */
public enum Role implements GrantedAuthority {

    /**
     * Роль пользователя
     */
    ROLE_USER,

    /**
     * Роль администратора
     */
    ROLE_ADMIN;

    /**
     * Метод интерфейса GrantedAuthority
     * Возвращает строковое представление роли
     * @return название роли
     */
    @Override
    public String getAuthority() {
        return name();
    }
}