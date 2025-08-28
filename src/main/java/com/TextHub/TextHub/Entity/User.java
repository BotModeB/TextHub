package com.TextHub.TextHub.Entity;

import jakarta.persistence.*;

import lombok.Data;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "users", 
        uniqueConstraints = {
            @UniqueConstraint(columnNames = "login", name = "uk_users_login"),
            @UniqueConstraint(columnNames = "email", name = "uk_users_email")
        })
@Data
@BatchSize(size = 20)
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 16, unique = true)
    private String login;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @CreationTimestamp
    @Column(name = "registration_date", nullable = false, updatable = false)
    private Instant registrationDate;

    @ColumnDefault("0")
    @Column(name = "post_count", nullable = false)
    private Integer postCount = 0;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();
}