package com.TextHub.TextHub.Service;

import com.TextHub.TextHub.Controller.CustomUserDetails;
import com.TextHub.TextHub.Entity.User;
import com.TextHub.TextHub.Entity.UserDTO;
import com.TextHub.TextHub.Repository.UserRepository;
import com.TextHub.TextHub.exceptions.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public User registerNewUser(UserDTO userDTO) {
        validateNewUser(userDTO);
        User user = createUserFromDTO(userDTO);
        return userRepository.save(user);
    }
    private void validateNewUser(UserDTO userDTO) {
        if (userRepository.existsByLogin(userDTO.getLogin())) {
            throw new IllegalArgumentException("Логин уже занят");
        }
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new IllegalArgumentException("Email уже используется");
        }
    }
    private User createUserFromDTO(UserDTO userDTO) {
        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setLogin(userDTO.getLogin());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return user;
    }
    public User findByLogin(String login) {
        return userRepository.findByLogin(login)
            .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));
    }
    
    public List<User> searchByLogin(String login) {
        return userRepository.findByLoginContainingIgnoreCase(login);
    }
    
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден с id: " + id));
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal().equals("anonymousUser")) {
            throw new IllegalStateException("Пользователь не аутентифицирован");
        }
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        return findByLogin(userDetails.getUsername());
    }
}