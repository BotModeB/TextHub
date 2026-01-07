package com.TextHub.TextHub.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.TextHub.TextHub.Entity.*;
import com.TextHub.TextHub.Repository.CommentsRepository;
import com.TextHub.TextHub.Repository.PostRepository;
import com.TextHub.TextHub.Repository.UserRepository;
import com.TextHub.TextHub.exceptions.ResourceNotFoundException;

@Service
@Transactional
public class CommentsServiceImpl implements CommentsService {
    private final CommentsRepository commentsRepository;
    private final UserService userService;
    private PostRepository postRepository;
    private UserRepository userRepository;

    public CommentsServiceImpl(CommentsRepository commentsRepository, UserService userService, PostRepository postRepository, UserRepository userRepository) {
        this.commentsRepository = commentsRepository;
        this.userService = userService;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void saveComment(CommentsDTO commentDTO) {
        Post post = postRepository.findById(commentDTO.getPostId())
                .orElseThrow(() -> new RuntimeException("Post not found with id: " + commentDTO.getPostId()));
        
        // Находим пользователя
        User user = userRepository.findById(commentDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + commentDTO.getUserId()));
        
        // Создаем и заполняем сущность комментария
        Comments comment = new Comments();
        comment.setContent(commentDTO.getContent());
        comment.setPost(post);  // Устанавливаем связь с постом
        comment.setUser(user);  // Устанавливаем связь с пользователем
        comment.setCreatedAt(Instant.now());
        
        commentsRepository.save(comment);
    }
    
    @Override
    public void deleteComment(Long id) {
        Comments Comments = getCommentById(id);
        validateCommentsOwnership(Comments);
        commentsRepository.deleteById(id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Comments> getAllComments() {
        return commentsRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Comments> getCommentsByPostId(Long id) {
        return commentsRepository.findByPostId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Comments getCommentById(Long id) {
        return commentsRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Comments not found with id: " + id));
    }


    public Comments updateComment(Long id, CommentsDTO CommentsDTO) {
        Comments existingComments = getCommentById(id);
        validateCommentsOwnership(existingComments);
        
        existingComments.setContent(CommentsDTO.getContent());
        existingComments.setUpdatedAt(LocalDateTime.now());
        
        return commentsRepository.save(existingComments);
    }


    private void validateCommentsOwnership(Comments Comments) {
        User currentUser = userService.getCurrentUser();
        if (!Comments.getUser().getId().equals(currentUser.getId())) {
            throw new SecurityException("You don't have permission to modify this Comments");
        }
    }

    // public CommentsDTO getCommentsById(Long id, Long currentUserId) {
    //     Comments Comments = CommentsRepository.findByIdWithUserAndLikes(id)
    //             .orElseThrow(() -> new EntityNotFoundException("Comments not found"));
        
    //     return CommentsDTO.fromComments(Comments, currentUserId);
    // }




}