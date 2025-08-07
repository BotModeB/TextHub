package com.TextHub.TextHub.Service;
import java.util.Map;
public interface LikeService {
    Map<String, Object> toggleLike(Long postId, Long userId);
    
    boolean isPostLikedByUser(Long postId, Long userId);
    
    int getLikesCount(Long postId);
}