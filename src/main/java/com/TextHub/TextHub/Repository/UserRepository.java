package com.TextHub.TextHub.Repository;

import com.TextHub.TextHub.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    boolean existsByLogin(String username);
    boolean existsByEmail(String email);
    List<User> findByLoginContainingIgnoreCase(String login);
    Optional<User> findById(Long id);
}