package com.TextHub.app.mapper;

import com.TextHub.TextHub.Entity.Chat;
import com.TextHub.TextHub.Entity.ChatSummaryDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Instant;

@Mapper(componentModel = "spring")
public interface ChatMapper {

    @Mapping(target = "chatId", source = "chat.id")
    @Mapping(target = "title", source = "resolvedTitle")
    @Mapping(target = "lastMessageAt", source = "lastMessageAt")
    @Mapping(target = "lastMessagePreview", source = "lastMessagePreview")
    @Mapping(target = "companionLogin", source = "companionLogin")
    @Mapping(target = "companionUsername", source = "companionUsername")
    ChatSummaryDTO toSummary(Chat chat,
                             String resolvedTitle,
                             Instant lastMessageAt,
                             String lastMessagePreview,
                             String companionLogin,
                             String companionUsername);
}
