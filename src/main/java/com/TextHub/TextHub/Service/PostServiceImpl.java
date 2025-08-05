package com.TextHub.TextHub.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.TextHub.TextHub.exceptions.ResourceNotFoundException;
import com.TextHub.TextHub.Repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Supplier;

import com.TextHub.TextHub.Repository.*;
import com.TextHub.TextHub.Entity.*;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository PostRepository;

    @Autowired
    public PostServiceImpl(PostRepository PostRepository) {
        this.PostRepository = PostRepository;
    }

    @Override
    public Post savePost(Post Post) {
        System.out.println("Saving Post: " + Post);
        Post saved = PostRepository.save(Post);
        System.out.println("Saved Post ID: " + saved.getId());
        return saved;
    }
    
    @Override
    public void deletePost(Long id) {
        PostRepository.deleteById(id);
    }

    @Override
    public List<Post> getAllPosts() {
        return PostRepository.findAll();
    }

    @Override
    public List<Post> getPostsByUser(User user) {
        return PostRepository.findByUser(user);
    }

    @Override
    @Transactional(readOnly = true)
    public Post getPostById(Long id) {
        Optional<Post> PostOptional = PostRepository.findById(id);
        return PostOptional.orElseThrow(() -> 
            new ResourceNotFoundException("Post not found with id: " + id));
    }

    @Override
    @Transactional
    public Post updatePost(Post post) {
        Optional<Post> postOptional = PostRepository.findById(post.getId());
        Post existingPost = postOptional.orElseThrow(() -> 
            new ResourceNotFoundException("Post not found with id: " + post.getId()));

        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        existingPost.setUpdatedAt(LocalDateTime.now());
        
        return PostRepository.save(existingPost);
    }
}    