package com.TextHub.TextHub.Entity;

import jakarta.persistence.*;
import lombok.Data;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
@Data
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "Post.withUserAndLikes",
        attributeNodes = {
            @NamedAttributeNode("user"),
            @NamedAttributeNode(value = "likes", subgraph = "likesSubgraph")
        },
        subgraphs = {
            @NamedSubgraph(
                name = "likesSubgraph",
                attributeNodes = @NamedAttributeNode("user")
            )
        }
    )
})
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 400)
    private String content;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20)
    private Set<Like> likes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    public boolean isLikedByUser(Long userId) {
        if (likes == null || userId == null) {
            return false;
        }
        return likes.stream()
                .anyMatch(like -> like.getUser() != null && 
                                userId.equals(like.getUser().getId()));
    }

    public int getLikesCount() {
        return likes.size();
    }

    public boolean isLikedBy(Long userId) {
        return likes.stream().anyMatch(like -> like.getUser().getId().equals(userId));
    }

    @Transient 
    private boolean likedByCurrentUser;
    
    public boolean isLikedByCurrentUser() {
        return likedByCurrentUser;
    }

    public void setLikedByCurrentUser(boolean likedByCurrentUser) {
        this.likedByCurrentUser = likedByCurrentUser;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }   

    @Transient
    public boolean isLikedByUser(User user) {
        return likes.stream().anyMatch(Like -> Like.getUser().equals(user));
    }
    
    @Transient 
    private Integer likesCount;
}