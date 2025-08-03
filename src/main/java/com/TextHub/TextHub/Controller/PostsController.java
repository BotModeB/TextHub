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
@RequestMapping("/posts")
public class PostsController {
    private final PostService PostService;  
    private final UserRepository UserRepository;
    private final UserService UserService;  

    
    @Autowired
    public PostsController(PostService PostService, UserService UserService, UserRepository UserRepository) {
        this.PostService = PostService;
        this.UserService = UserService;
        this.UserRepository = UserRepository;
    }
    
    @GetMapping("/public")
    public String showPostsPage(Model model) {
        List<Post> posts = PostService.getAllPosts();
        model.addAttribute("posts", posts); 
        return "posts";
    }

    @GetMapping("/user-posts")
    public String showUserPostsPage(
        Model model,
        Authentication authentication) {

        String login = authentication.getName();

        User user = UserRepository.findByLogin(login)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Post> posts = PostService.getPostsByUser(user);
        
        model.addAttribute("posts", posts); 
        return "user-posts";
    }

    @GetMapping("/form")
    public String showForm(@RequestParam(required = false) Long id, Model model) {
        Post post = id != null ? PostService.getPostById(id) : new Post();
        model.addAttribute("post", post);
        model.addAttribute("isEditMode", id != null);
        return "post-form";
    }
    
    @PostMapping("/save")
    public String savePost(
        @ModelAttribute Post post,
        Authentication authentication,
        RedirectAttributes redirectAttributes
    ) {

        User user = UserService.findByLogin(authentication.getName());
        post.setUser(user);
        
        if (post.getId() != null) {
            PostService.updatePost(post);
        } else {
            PostService.savePost(post);
        }
        redirectAttributes.addFlashAttribute("message", 
            post.getId() != null ? "Пост обновлен" : "Пост создан");
        
        return "redirect:/posts/public";
    }
    
    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable Long id) {
        Post post = PostService.getPostById(id);
        PostService.deletePost(id);
        return "redirect:/posts/public";
    }
    
}