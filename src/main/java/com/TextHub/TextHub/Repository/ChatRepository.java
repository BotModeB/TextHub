package com.TextHub.TextHub.Repository;

import com.TextHub.TextHub.Entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<Chat, Long> {
}
