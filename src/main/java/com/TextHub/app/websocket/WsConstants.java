package com.TextHub.app.websocket;

public final class WsConstants {
    private WsConstants() {}

    public static final String ENDPOINT = "/ws";
    public static final String APP_PREFIX = "/app";
    public static final String TOPIC_PREFIX = "/topic";
    public static final String CHAT_DESTINATION = "/chats/{chatId}";
}
