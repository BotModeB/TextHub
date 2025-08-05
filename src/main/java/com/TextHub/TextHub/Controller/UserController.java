package com.TextHub.TextHub.Controller;

import com.TextHub.TextHub.Entity.*;
import com.TextHub.TextHub.Repository.*;
import com.TextHub.TextHub.Service.*;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;

@Controller
@RequestMapping("/users")
public class UserController {
        
    private final PostService PostService;  
    private final UserRepository UserRepository;
    private final UserService UserService;  

    
    @Autowired
    public UserController(PostService PostService, UserService UserService, UserRepository UserRepository) {
        this.PostService = PostService;
        this.UserService = UserService;
        this.UserRepository = UserRepository;
    }


    @GetMapping("profile/{identifier}")
    public String showProfile(@PathVariable String identifier,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        
        try {
            User user = UserService.findByLogin(identifier);
            
            model.addAttribute("user", user);
            return "profile";
            
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/users";
        }
    }

    @GetMapping
    public String searchUsers(
        @RequestParam(required = false) String query,
        Model model
    ) {
        if (query != null && !query.isEmpty()) {
            List<User> users = UserService.searchByLogin(query);
            model.addAttribute("users", users);
            model.addAttribute("query", query);
        }
        return "users";
    }

}