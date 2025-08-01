package com.TextHub.TextHub.Entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "users", 
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "login", name = "uk_users_login"),
           @UniqueConstraint(columnNames = "email", name = "uk_users_email")
       })
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, length = 50)
    private String username;

    @Column(nullable = false, length = 16,unique = true)
    private String login;

    @Column(nullable = false, length = 100,unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @CreationTimestamp
    @Column(name = "registration_date", nullable = false, updatable = false)
    private Instant registrationDate;

    @ColumnDefault("0")
    @Column(name = "post_count", nullable = false)
    private Integer postCount = 0;

    // public void setPassword(String password) {
    //     this.password = new BCryptPasswordEncoder().encode(password);
    // }
    
}