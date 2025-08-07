package com.TextHub.TextHub.Service;
import com.TextHub.TextHub.Repository.LikeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;
@Service
@Transactional
public class LikeServiceImpl implements LikeService {
    
    private final LikeRepository likeRepository;
    
    public LikeServiceImpl(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }
    
    @Override
    public Map<String, Object> toggleLike(Long postId, Long userId) {
        boolean isLiked = likeRepository.existsByUserAndPost(userId, postId);
        Map<String, Object> response = new HashMap<>();
        
        if (isLiked) {
            likeRepository.deleteByUserIdAndPostId(userId, postId);
        } else {
            likeRepository.addLike(userId, postId);
        }
        
        response.put("isLikedByCurrentUser", !isLiked);
        response.put("likesCount", getLikesCount(postId));
        return response;
    }
    
    @Override
    public boolean isPostLikedByUser(Long postId, Long userId) {
        return likeRepository.existsByUserAndPost(userId, postId);
    }
    
    @Override
    public int getLikesCount(Long postId) {
        return likeRepository.countByPostId(postId);
    }
}