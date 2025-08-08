package com.TextHub.TextHub.Service;
import com.TextHub.TextHub.exceptions.ResourceNotFoundException;
import com.TextHub.TextHub.Repository.PostRepository;
import com.TextHub.TextHub.Entity.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserService userService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Override
    @Transactional
    public Post savePost(PostDTO postDTO) {
        Post post = new Post();
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        User currentUser = userService.getCurrentUser();
        post.setUser(currentUser);
        
        currentUser.setPostCount(currentUser.getPostCount() + 1);
        
        return postRepository.save(post);
    }
    
    @Override
    public void deletePost(Long id) {
        Post post = getPostById(id);
        validatePostOwnership(post);
        postRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Post> getPostsByUser(User user) {
        return postRepository.findByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Post getPostById(Long id) {
        return postRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
    }

    @Override
    @Transactional
    public Post updatePost(Long id, PostDTO postDTO) {
        Post existingPost = getPostById(id);
        validatePostOwnership(existingPost);
        
        existingPost.setTitle(postDTO.getTitle());
        existingPost.setContent(postDTO.getContent());
        existingPost.setUpdatedAt(LocalDateTime.now());
        
        return postRepository.save(existingPost);
    }
    
    private void validatePostOwnership(Post post) {
        User currentUser = userService.getCurrentUser();
        if (!post.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to modify this post");
        }
    }
}