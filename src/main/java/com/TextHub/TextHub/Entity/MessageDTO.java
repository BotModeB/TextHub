package com.TextHub.TextHub.Entity;

import java.time.Instant;

import lombok.Data;

@Data
public class MessageDTO {
    private Long id;
    private Long chatId;
    private Long senderId;
    private String senderLogin;
    private String senderUsername;
    private String content;
    private Instant createdAt;
}
