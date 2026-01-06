package com.TextHub.TextHub.Service;

import com.TextHub.TextHub.Entity.Chat;
import com.TextHub.TextHub.Entity.ChatSummaryDTO;
import com.TextHub.TextHub.Entity.MessageDTO;

import java.util.List;

public interface ChatService {
    Chat getOrCreatePrivateChatWith(String otherLogin);
    List<ChatSummaryDTO> getUserChats();
    Chat getChatForCurrentUser(Long chatId);
    List<MessageDTO> getChatMessages(Long chatId);

    /**
     * Изменить название чата. Разрешено только участникам чата.
     *
     * @param chatId  идентификатор чата
     * @param newTitle новое название (может быть null/пустым для сброса к имени собеседника)
     */
    void renameChat(Long chatId, String newTitle);
}
