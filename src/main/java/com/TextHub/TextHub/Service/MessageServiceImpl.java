package com.TextHub.TextHub.Service;

import com.TextHub.TextHub.Entity.Chat;
import com.TextHub.TextHub.Entity.Message;
import com.TextHub.TextHub.Entity.MessageDTO;
import com.TextHub.TextHub.Entity.User;
import com.TextHub.TextHub.Repository.ChatRepository;
import com.TextHub.TextHub.Repository.MessageRepository;
import com.TextHub.TextHub.exceptions.ResourceNotFoundException;
import com.TextHub.app.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final com.TextHub.TextHub.Repository.UserRepository userRepository;
    private final MessageMapper messageMapper;

    @Override
    public MessageDTO sendMessage(Long chatId, String content, String senderLogin) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Сообщение не может быть пустым");
        }

        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Чат не найден"));
        User sender = userRepository.findByLogin(senderLogin)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        Message message = new Message();
        message.setChat(chat);
        message.setSender(sender);
        message.setContent(content.trim());

        Message saved = messageRepository.save(message);
        return messageMapper.toDto(saved);
    }
}
