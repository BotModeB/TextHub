package com.TextHub.TextHub.Service;
import com.TextHub.TextHub.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

import com.TextHub.TextHub.Repository.PostRepository;
import com.TextHub.TextHub.Entity.*;
import com.TextHub.app.mapper.PostMapper;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserService userService;
    private final PostMapper postMapper;

    public PostServiceImpl(PostRepository postRepository, UserService userService, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.userService = userService;
        this.postMapper = postMapper;
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


    public Page<PostDTO> getPosts(Pageable pageable, Long currentUserId) {
        Page<Post> posts = postRepository.findAllWithUserAndLikes(pageable);
        
        return posts.map(post -> postMapper.toDto(post, currentUserId));
    }

    public PostDTO getPostById(Long id, Long currentUserId) {
        Post post = postRepository.findByIdWithUserAndLikes(id)
                .orElseThrow(() -> new EntityNotFoundException("Post not found"));
        
        return postMapper.toDto(post, currentUserId);
    }

    @Override
    public Post getPostWithUserAndComments(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Post ID cannot be null");
        }
        
        return postRepository.findPostWithUserAndComments(id)
                .orElseThrow(() -> new EntityNotFoundException("Пост с ID " + id + " не найден"));
    }
}