package com.TextHub.TextHub.Controller;

import com.TextHub.TextHub.Entity.*;
import com.TextHub.TextHub.Repository.*;
import com.TextHub.TextHub.Service.*;

import lombok.RequiredArgsConstructor;

import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/posts")
public class PostsController {
    private final PostService PostService;  
    private final UserRepository UserRepository;
    private final UserService UserService; 
    private final LikeRepository LikeRepository;
    private final PostRepository PostRepository;

    
    @Autowired
    public PostsController(PostService PostService, UserService UserService, UserRepository UserRepository, LikeRepository LikeRepository, PostRepository PostRepository) {
        this.PostService = PostService;
        this.UserService = UserService;
        this.UserRepository = UserRepository;
        this.LikeRepository = LikeRepository;
        this.PostRepository = PostRepository;
    }
    
    @GetMapping("/public")
    public String showPostsPage(Model model, 
                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        // Загружаем посты без лайков (только основные данные)
        List<Post> posts = PostService.getAllPosts();
        
        // Для каждого поста загружаем лайки отдельно
        for (Post post : posts) {
            int likesCount = LikeRepository.countByPostId(post.getId());
            post.setLikesCount(likesCount);
            
            if (userDetails != null) {
                boolean isLiked = LikeRepository.existsByUserAndPost(
                    userDetails.getUserId(), post.getId());
                post.setLikedByCurrentUser(isLiked);
            }
        }
        
        model.addAttribute("posts", posts);
        model.addAttribute("currentUserId", userDetails != null ? userDetails.getUserId() : null);
        
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
        
    @PostMapping("/{postId}/like")
    @Transactional
    public ResponseEntity<?> toggleLike(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        
        try {
            if (userDetails == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            User user = UserRepository.findById(userDetails.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Post post = PostRepository.findById(postId)
                    .orElseThrow(() -> new RuntimeException("Post not found"));
            
            // Проверяем, есть ли лайк от этого пользователя для этого поста
            boolean liked = false;
            Optional<Like> existingLike = LikeRepository.findByUserAndPost(user, post);
            
            if (existingLike.isPresent()) {
                // Удаляем существующий лайк
                LikeRepository.delete(existingLike.get());
                liked = false;
            } else {
                // Создаем новый лайк
                Like newLike = new Like();
                newLike.setUser(user);
                newLike.setPost(post);
                LikeRepository.save(newLike);
                liked = true;
            }

            // Получаем обновленное количество лайков
            int likesCount = LikeRepository.countByPostId(postId);
            
            Map<String, Object> response = new HashMap<>();
            response.put("likesCount", likesCount);
            response.put("isLikedByCurrentUser", liked);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}