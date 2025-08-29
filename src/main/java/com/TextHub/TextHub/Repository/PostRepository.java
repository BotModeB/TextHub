package com.TextHub.TextHub.Repository;
import com.TextHub.TextHub.Entity.Post;
import com.TextHub.TextHub.Entity.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUser(User user);
    Optional<Post> findById(Long id);

    @EntityGraph(value = "Post.withUserAndLikes", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT p FROM Post p ORDER BY p.createdAt DESC")
    Page<Post> findAllWithUserAndLikes(Pageable pageable);
    
    @Query("SELECT DISTINCT p FROM Post p LEFT JOIN FETCH p.user LEFT JOIN FETCH p.likes WHERE p.id = :id")
    Optional<Post> findByIdWithUserAndLikes(@Param("id") Long id);
    
    @Query("SELECT COUNT(l) FROM Like l WHERE l.post.id = :postId")
    int countLikesByPostId(@Param("postId") Long postId);
    
    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END FROM Like l WHERE l.post.id = :postId AND l.user.id = :userId")
    boolean existsByPostIdAndUserId(@Param("postId") Long postId, @Param("userId") Long userId);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.user LEFT JOIN FETCH p.comments WHERE p.id = :id")
    Optional<Post> findPostWithUserAndComments(@Param("id") Long id);
}