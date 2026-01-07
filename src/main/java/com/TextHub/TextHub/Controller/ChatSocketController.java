package com.TextHub.TextHub.Controller;

import com.TextHub.TextHub.Entity.MessageDTO;
import com.TextHub.TextHub.Service.MessageService;
import com.TextHub.app.websocket.ChatMessagePayload;
import com.TextHub.app.websocket.WsConstants;
import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatSocketController {

    private final MessageService messageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping(WsConstants.CHAT_DESTINATION)
    public void handleChatMessage(@DestinationVariable Long chatId,
                                  @Valid @Payload ChatMessagePayload payload,
                                  Principal principal) {
        MessageDTO saved = messageService.sendMessage(chatId, payload.getContent(), principal.getName());
        messagingTemplate.convertAndSend(WsConstants.TOPIC_PREFIX + "/chats/" + chatId, saved);
    }
}
