package com.TextHub.TextHub.Controller;

import com.TextHub.TextHub.Entity.MessageDTO;
import com.TextHub.TextHub.Service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chats/{chatId}")
    public void handleChatMessage(@DestinationVariable Long chatId, MessageDTO payload, Principal principal) {
        // Пока упрощаем: доверяем фронтенду, что пользователь пишет только в свои чаты.
        // main-проверка осуществляется на обычных HTTP-запросах.
        MessageDTO saved = messageService.sendMessage(chatId, payload.getContent(), principal.getName());
        messagingTemplate.convertAndSend("/topic/chats/" + chatId, saved);
    }
}
