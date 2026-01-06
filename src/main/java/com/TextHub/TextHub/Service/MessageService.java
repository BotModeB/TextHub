package com.TextHub.TextHub.Service;

import com.TextHub.TextHub.Entity.MessageDTO;

public interface MessageService {
    MessageDTO sendMessage(Long chatId, String content, String senderLogin);
}
