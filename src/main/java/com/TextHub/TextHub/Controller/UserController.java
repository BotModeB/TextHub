package com.TextHub.TextHub.Controller;

import com.TextHub.TextHub.Entity.*;
import com.TextHub.TextHub.Service.*;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService userService;  

    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/{login}/profile")
    public String showProfile(@PathVariable String login,
                            Model model,
                            RedirectAttributes redirectAttributes) {

        
        try {
            User user = userService.findByLogin(login);
            
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
            List<User> users = userService.searchByLogin(query);
            model.addAttribute("users", users);
            model.addAttribute("query", query);
        }
        return "users";
    }

}