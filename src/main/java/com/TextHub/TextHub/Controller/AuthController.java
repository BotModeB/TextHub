package com.TextHub.TextHub.Controller;

import com.TextHub.TextHub.Entity.User;
import com.TextHub.TextHub.Repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping({"/", "/home", "/index.html"})
    public String index() {
        return "index"; 
    }

    @GetMapping("/register")
    public String register() {
        return "registrations";
    }

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Неверные учетные данные");
        }
        return "login";
    }

    @PostMapping("/register")
    public String registerUser(
        @RequestParam String username,
        @RequestParam String login,
        @RequestParam String email,
        @RequestParam String password,
        RedirectAttributes redirectAttributes) {
        
        try {
            if (userRepository.existsByLogin(login)) {
                redirectAttributes.addFlashAttribute("error", "Логин уже занят");
                return "redirect:/register";
            }

            if (userRepository.existsByEmail(email)) {
                redirectAttributes.addFlashAttribute("error", "Email уже используется");
                return "redirect:/register";
            }

            User user = new User();
            user.setUsername(username);
            user.setLogin(login);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            
            userRepository.save(user);
            
            redirectAttributes.addFlashAttribute("success", "Регистрация успешна! Войдите в систему.");
            return "redirect:/login";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при регистрации: " + e.getMessage());
            return "redirect:/register";
        }
    }
}