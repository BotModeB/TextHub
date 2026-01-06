package com.TextHub.TextHub.Repository;

import com.TextHub.TextHub.Entity.Chat;
import com.TextHub.TextHub.Entity.ChatMember;
import com.TextHub.TextHub.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {
    boolean existsByChatAndUser(Chat chat, User user);
    List<ChatMember> findByUser(User user);
    Optional<ChatMember> findByChatAndUser(Chat chat, User user);
    List<ChatMember> findByChat(Chat chat);

    @Query("SELECT cm.chat FROM ChatMember cm WHERE cm.user = :user")
    List<Chat> findChatsByUser(@Param("user") User user);
}
