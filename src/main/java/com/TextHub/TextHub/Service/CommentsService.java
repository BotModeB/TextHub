package com.TextHub.TextHub.Service;

import java.util.List;

import com.TextHub.TextHub.Entity.*;

public interface CommentsService {
    void saveComment(CommentsDTO CommentsDTO);
    List<Comments> getAllComments();
    void deleteComment(Long id);
    List<Comments> getCommentsByPostId(Long id);
    Comments getCommentById(Long id);
    Comments updateComment(Long id, CommentsDTO CommentDTO);
    
} 
