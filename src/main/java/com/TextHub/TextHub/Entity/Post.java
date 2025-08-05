package com.TextHub.TextHub.Entity;

import com.TextHub.TextHub.Entity.*;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "posts")
@Data
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
    private Instant date;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Like> likes = new HashSet<>();

    public boolean isLikedBy(Long userId) {
        return likes.stream().anyMatch(like -> like.getUser().getId().equals(userId));
    }

    @Transient // Поле не сохраняется в БД
    private boolean likedByCurrentUser;
    
    // Геттеры и сеттеры
    public boolean isLikedByCurrentUser() {
        return likedByCurrentUser;
    }

    public void setLikedByCurrentUser(boolean likedByCurrentUser) {
        this.likedByCurrentUser = likedByCurrentUser;
    }

    public int getLikesCount() {
        return likesCount;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    
    // Геттеры и сеттеры
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
}