package com.TextHub.TextHub.Entity;
import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class PostDTO {
    private Long id;
    
    @NotBlank(message = "Title cannot be empty")
    @Size(min = 3, max = 50, message = "Title must be between 3 and 50 characters")
    private String title;
    
    @NotBlank(message = "Content cannot be empty")
    @Size(min = 10, max = 400, message = "Content must be between 10 and 400 characters")
    private String content;
    
    private Long userId;
    private String username;
    private int likesCount;
    private boolean likedByCurrentUser;
    private User user;
    private Instant createdAt;
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public static PostDTO fromPost(Post post, Long currentUserId) {
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setUser(post.getUser());
        dto.setCreatedAt(post.getCreatedAt()); 
        
        if (post.getUser() != null) {
            dto.setUserId(post.getUser().getId());
            dto.setUsername(post.getUser().getUsername());
        }
        
        dto.setLikesCount(post.getLikesCount());
        dto.setLikedByCurrentUser(currentUserId != null && 
                                post.isLikedByUser(currentUserId));
        
        return dto;
    }
}