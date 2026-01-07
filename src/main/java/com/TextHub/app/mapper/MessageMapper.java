package com.TextHub.app.mapper;

import com.TextHub.TextHub.Entity.Message;
import com.TextHub.TextHub.Entity.MessageDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {

    @Mapping(target = "chatId", source = "chat.id")
    @Mapping(target = "senderId", source = "sender.id")
    @Mapping(target = "senderLogin", source = "sender.login")
    @Mapping(target = "senderUsername", source = "sender.username")
    MessageDTO toDto(Message message);

    List<MessageDTO> toDtoList(List<Message> messages);
}
