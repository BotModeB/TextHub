package com.TextHub.TextHub.Repository;
import com.TextHub.TextHub.Entity.Post;
import com.TextHub.TextHub.Entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);
    Optional<Post> findById(Long id);
}