package com.TextHub.TextHub.Controller;

import com.TextHub.TextHub.Entity.*;
import com.TextHub.TextHub.Service.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
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
    private final CommentsService commentsService;
    private final UserService userService;

    public PostsController(PostService postService, LikeService likeService, CommentsService commentsService, UserService userService) {
        this.postService = postService;
        this.likeService = likeService;
        this.userService = userService;
        this.commentsService = commentsService;
    }
    
    @GetMapping("/public")
    public Object getPosts(
        @PageableDefault(size = 10, sort = "createdAt", direction = Direction.DESC) Pageable pageable,
        @AuthenticationPrincipal CustomUserDetails userDetails,
        Model model,
        HttpServletRequest request) {
    Long currentUserId = userDetails != null ? userDetails.getUserId() : null;
    Page<PostDTO> postsPage = postService.getPosts(pageable, currentUserId);
    
    model.addAttribute("postsPage", postsPage);
    model.addAttribute("posts", postsPage.getContent()); 
    model.addAttribute("currentUserId", currentUserId);
    
    return "posts";
} 
    @GetMapping("/form")
    public String showForm(@RequestParam(required = false) Long id, Model model) {
        Post post = new Post();
        if (id != null) {
            post = postService.getPostById(id);
        }
        model.addAttribute("post", post);
        return "post-form";
    }

    @GetMapping("{login}/channel")
    public String ShowChannelByUser(@PathVariable String login, @AuthenticationPrincipal CustomUserDetails userDetails, Model model){
        User channelUser = userService.findByLogin(login);
        List<Post> posts = postService.getPostsByUser(channelUser);
                
        Long currentUserId = userDetails != null ? userDetails.getUserId() : null;
        for (Post post : posts) {
            if (currentUserId != null) {
                boolean isLiked = post.getLikes().stream()
                        .anyMatch(like -> like.getUser().getId().equals(currentUserId));
                post.setLikedByCurrentUser(isLiked);
            }
            post.setLikesCount(post.getLikes().size());
            
        }
        
        model.addAttribute("channelUser", channelUser);
        model.addAttribute("posts", posts);
        model.addAttribute("currentUserId", currentUserId);
        
        return "channel"; 
        
    }
    
    @GetMapping("/{id}/page")
    public String ShowPagePost(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
        Long currentUserId = userDetails != null ? userDetails.getUserId() : null;
        Post post = postService.getPostWithUserAndComments(id);
        if (currentUserId != null) {
            boolean isLiked = post.getLikes().stream()
                    .anyMatch(like -> like.getUser().getId().equals(currentUserId));
            post.setLikedByCurrentUser(isLiked);
        }
        model.addAttribute("post", post);
        model.addAttribute("currentUserId", currentUserId);
        model.addAttribute("comment", new Comments()); 
        
        return "post-page"; 
    }

    @PostMapping("/{id}/page")
    public String saveComment(@PathVariable Long id,
                            @ModelAttribute("comment") Comments comment,
                            @AuthenticationPrincipal CustomUserDetails userDetails,
                            RedirectAttributes redirectAttributes) {
        
        try {
            // Создаем DTO и заполняем его
            CommentsDTO commentDTO = new CommentsDTO();
            commentDTO.setContent(comment.getContent());
            commentDTO.setPostId(id);
            commentDTO.setUserId(userDetails.getUserId());
            
            commentsService.saveComment(commentDTO);
            
            redirectAttributes.addFlashAttribute("message", "Комментарий добавлен");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка при добавлении комментария: " + e.getMessage());
        }
        
        return "redirect:/posts/" + id + "/page";
    }

    @PostMapping("/save")
    public String savePost(@Valid @ModelAttribute("post") Post post,
                            BindingResult bindingResult,
                            RedirectAttributes redirectAttributes,
                            @AuthenticationPrincipal CustomUserDetails userDetails) {
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
            return "redirect:/posts/" + userDetails.getUsername() + "/channel";
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