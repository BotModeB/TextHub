package com.TextHub.TextHub.Controller;

import com.TextHub.TextHub.Entity.*;
import com.TextHub.TextHub.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Controller
@RequestMapping("/posts")
public class PostsController {
    private final PostService postService;
    private final LikeService likeService;
    @Autowired
    public PostsController(PostService postService, LikeService likeService) {
        this.postService = postService;
        this.likeService = likeService;
    }
    
    // @GetMapping("/public")
    // public String showPostsPage(Model model, 
    //                         @AuthenticationPrincipal CustomUserDetails userDetails) {
    //     List<Post> posts = postService.getAllPosts();
    //     Long currentUserId = userDetails != null ? userDetails.getUserId() : null;
        
    //     // Проверяем статус лайков для каждого поста
    //     for (Post post : posts) {
    //         if (currentUserId != null) {
    //             post.setLikedByCurrentUser(post.isLikedBy(currentUserId));
    //         }
    //         // Обновляем счетчик лайков
    //         post.setLikesCount(post.getLikes().size());
    //     }
        
    //     model.addAttribute("posts", posts);
    //     model.addAttribute("currentUserId", currentUserId);
    //     return "posts";
    // }

@GetMapping("/public")
public Object getPosts(
        @PageableDefault(size = 10, sort = "createdAt", direction = Direction.DESC) Pageable pageable,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        Model model,
        HttpServletRequest request) {
    Long currentUserId = userDetails != null ? userDetails.getUserId() : null;
    Page<PostDTO> postsPage = postService.getPosts(pageable, currentUserId);
    
    model.addAttribute("postsPage", postsPage);
    model.addAttribute("posts", postsPage.getContent()); // Вот это список!
    model.addAttribute("currentUserId", currentUserId);
    
    return "posts";
} 
    // @GetMapping("/user-posts")
    // public String showUserPostsPage(Model model, 
    //                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
    //     List<Post> posts = postService.getPostsByUser(userDetails.getUser());
    //     model.addAttribute("posts", posts);
    //     return "user-posts";
    // }

    @GetMapping("/form")
    public String showForm(@RequestParam(required = false) Long id, Model model) {
        Post post = new Post();
        if (id != null) {
            post = postService.getPostById(id);
        }
        model.addAttribute("post", post);
        return "post-form";
    }

    @PostMapping("/save")
    public String savePost(@Valid @ModelAttribute("post") Post post,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "post-form";
        }
        
        try {
            PostDTO postDTO = new PostDTO();
            postDTO.setTitle(post.getTitle());
            postDTO.setContent(post.getContent());
            
            if (post.getId() != null) {
                postService.updatePost(post.getId(), postDTO);
                redirectAttributes.addFlashAttribute("message", "Пост успешно обновлен");
            } else {
                postService.savePost(postDTO);
                redirectAttributes.addFlashAttribute("message", "Пост успешно создан");
            }
            return "redirect:/posts/public";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при сохранении поста: " + e.getMessage());
            return "redirect:/posts/form";
        }
    }
    
    @PostMapping("/{postId}/delete")
    public String deletePost(@PathVariable Long postId,
                            RedirectAttributes redirectAttributes) {
        try {
            postService.deletePost(postId);
            redirectAttributes.addFlashAttribute("message", "Post deleted");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/posts/public";
    }
        
    @PostMapping("/{postId}/like")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> toggleLike(@PathVariable Long postId,
                                    @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        
        try {
            Map<String, Object> response = likeService.toggleLike(postId, userDetails.getUserId());
            
            if (response == null) {
                throw new RuntimeException("Не удалось обработать лайк");
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Произошла ошибка при обновлении лайка: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
}