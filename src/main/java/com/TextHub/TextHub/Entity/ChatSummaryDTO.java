package com.TextHub.TextHub.Entity;

import lombok.Data;

import java.time.Instant;

@Data
public class ChatSummaryDTO {
    private Long chatId;
    private String title;
    private Instant lastMessageAt;
    private String lastMessagePreview;
    private String companionLogin;
    private String companionUsername;
}
