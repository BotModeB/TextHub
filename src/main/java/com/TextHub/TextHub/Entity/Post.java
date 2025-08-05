package com.TextHub.TextHub.Entity;

import com.TextHub.TextHub.Entity.*;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
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

    @Transient
    public int getLikesCount() {
        return likes.size();
    }

    @Transient
    public boolean isLikedByUser(User user) {
        return likes.stream().anyMatch(Like -> Like.getUser().equals(user));
    }
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