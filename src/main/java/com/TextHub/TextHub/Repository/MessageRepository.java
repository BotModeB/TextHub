package com.TextHub.TextHub.Repository;

import com.TextHub.TextHub.Entity.Chat;
import com.TextHub.TextHub.Entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query("SELECT m FROM Message m WHERE m.chat = :chat ORDER BY m.createdAt DESC")
    List<Message> findRecentByChat(@Param("chat") Chat chat, Pageable pageable);

    @Query("SELECT m FROM Message m WHERE m.chat = :chat ORDER BY m.createdAt ASC")
    List<Message> findAllByChatOrderByCreatedAtAsc(@Param("chat") Chat chat);

    @Query("SELECT m FROM Message m WHERE m.chat = :chat ORDER BY m.createdAt DESC")
    List<Message> findLatestMessage(@Param("chat") Chat chat, Pageable pageable);
}
