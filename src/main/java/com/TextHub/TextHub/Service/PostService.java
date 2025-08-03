package com.TextHub.TextHub.Service;

import  com.TextHub.TextHub.Entity.*;

import java.util.List;

public interface PostService {
    Post savePost(Post Post);
    List<Post> getAllPosts();
    void deletePost(Long id);
    List<Post> getPostsByUser(User user);
    Post getPostById(Long id);
    Post updatePost(Post Post);
    
}