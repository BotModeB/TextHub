package com.TextHub.TextHub.Service;

import com.TextHub.TextHub.Entity.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {
    Post savePost(PostDTO postDTO);
    List<Post> getAllPosts();
    void deletePost(Long id);
    List<Post> getPostsByUser(User user);
    Post getPostById(Long id);
    Post updatePost(Long id, PostDTO postDTO);
    Page<PostDTO> getPosts(Pageable pageable, Long currentUserId);
    PostDTO getPostById(Long id, Long currentUserId);
    Post getPostWithUserAndComments(Long id);
}