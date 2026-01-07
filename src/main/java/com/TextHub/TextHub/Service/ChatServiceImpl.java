package com.TextHub.TextHub.Service;

import com.TextHub.TextHub.Entity.*;
import com.TextHub.TextHub.Repository.ChatMemberRepository;
import com.TextHub.TextHub.Repository.ChatRepository;
import com.TextHub.TextHub.Repository.MessageRepository;
import com.TextHub.TextHub.Repository.UserRepository;
import com.TextHub.TextHub.exceptions.ResourceNotFoundException;
import com.TextHub.app.mapper.ChatMapper;
import com.TextHub.app.mapper.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final ChatMemberRepository chatMemberRepository;
    private final MessageRepository messageRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final MessageMapper messageMapper;
    private final ChatMapper chatMapper;

    @Override
    public Chat getOrCreatePrivateChatWith(String otherLogin) {
        User currentUser = userService.getCurrentUser();
        User other = userRepository.findByLogin(otherLogin)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не найден"));

        List<Chat> currentChats = chatMemberRepository.findChatsByUser(currentUser);
        Optional<Chat> existing = currentChats.stream()
                .filter(c -> chatMemberRepository.existsByChatAndUser(c, other))
                .findFirst();

        if (existing.isPresent()) {
            return existing.get();
        }

        Chat chat = new Chat();
        chat.setTitle(null); // приватный чат — показываем имя собеседника в UI
        Chat saved = chatRepository.save(chat);

        ChatMember cm1 = new ChatMember();
        cm1.setChat(saved);
        cm1.setUser(currentUser);

        ChatMember cm2 = new ChatMember();
        cm2.setChat(saved);
        cm2.setUser(other);

        chatMemberRepository.save(cm1);
        chatMemberRepository.save(cm2);
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatSummaryDTO> getUserChats() {
        User current = userService.getCurrentUser();
        List<Chat> chats = chatMemberRepository.findChatsByUser(current);

        return chats.stream()
                .map(chat -> buildSummary(chat, current))
                .sorted(Comparator.comparing(ChatSummaryDTO::getLastMessageAt,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Chat getChatForCurrentUser(Long chatId) {
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ResourceNotFoundException("Чат не найден"));
        User current = userService.getCurrentUser();
        if (!chatMemberRepository.existsByChatAndUser(chat, current)) {
            throw new SecurityException("Нет доступа к чату");
        }
        return chat;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageDTO> getChatMessages(Long chatId) {
        Chat chat = getChatForCurrentUser(chatId);
        return messageMapper.toDtoList(messageRepository.findAllByChatOrderByCreatedAtAsc(chat));
    }

    @Override
    public void renameChat(Long chatId, String newTitle) {
        Chat chat = getChatForCurrentUser(chatId); // проверка, что текущий пользователь участник

        String title = (newTitle != null ? newTitle.trim() : null);
        if (title != null && title.isEmpty()) {
            title = null; // пустую строку считаем как отсутствие кастомного названия
        }

        chat.setTitle(title);
        // chat находится в persistence context (@Transactional), поэтому отдельный save не обязателен,
        // но можно явно сохранить для ясности:
        chatRepository.save(chat);
    }

    private ChatSummaryDTO buildSummary(Chat chat, User current) {
        List<ChatMember> members = chatMemberRepository.findByChat(chat);

        // companion for приватного чата (2 участника)
        ChatMember companion = members.stream()
                .filter(cm -> !cm.getUser().getId().equals(current.getId()))
                .findFirst()
                .orElse(null);

        String resolvedTitle = chat.getTitle();
        String companionLogin = null;
        String companionUsername = null;
        if (companion != null) {
            companionLogin = companion.getUser().getLogin();
            companionUsername = companion.getUser().getUsername();
            if (resolvedTitle == null) {
                resolvedTitle = companionUsername;
            }
        }

        List<Message> latest = messageRepository.findLatestMessage(chat, PageRequest.of(0, 1));
        Message lm = latest.isEmpty() ? null : latest.get(0);
        String preview = null;
        java.time.Instant lastAt = null;
        if (lm != null) {
            lastAt = lm.getCreatedAt();
            String content = lm.getContent();
            preview = content.length() > 80 ? content.substring(0, 80) + "…" : content;
        }

        return chatMapper.toSummary(chat,
                resolvedTitle,
                lastAt,
                preview,
                companionLogin,
                companionUsername);
    }
}
