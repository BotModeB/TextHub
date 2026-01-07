package com.TextHub.app.websocket;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatMessagePayload {
    @NotBlank
    private String content;
}
