package com.TextHub.TextHub.Service;

import com.TextHub.TextHub.Entity.User;
import com.TextHub.TextHub.Repository.UserRepository;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public User findByLogin(String login) {
        return userRepository.findByLogin(login)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
    
    public List<User> searchByLogin(String login) {
        return userRepository.findByLoginContainingIgnoreCase(login);
    }

    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
}