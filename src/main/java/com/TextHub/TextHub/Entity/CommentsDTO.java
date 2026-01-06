package com.TextHub.TextHub.Entity;
import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
@Data
public class CommentsDTO {
    private Long id;
    
    
    @NotBlank(message = "Content cannot be empty")
    @Size(min = 10, max = 400, message = "Content must be between 10 and 400 characters")
    private String content;
    private Long postId;
    private Long userId;
    private Instant createdAt;
}