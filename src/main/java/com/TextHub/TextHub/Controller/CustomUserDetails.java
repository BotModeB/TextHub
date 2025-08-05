package com.TextHub.TextHub.Controller;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.TextHub.TextHub.Entity.User;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Временная реализация - возвращаем одну роль "ROLE_USER" для всех пользователей
        // В реальном приложении роли должны храниться в базе данных
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
    
    @Override
    public String getUsername() {
        return user.getLogin(); // Используем login как username
    }

    // Для получения email 
    public String getEmail() {
        return user.getEmail();
    }

    // Для получения ID пользователя
    public Long getUserId() {
        return user.getId();
    }

    
}
