package com.TextHub.TextHub;

import com.TextHub.TextHub.config.WebSocketConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class WebSocketConfigTest {

    @Autowired
    private WebSocketConfig webSocketConfig;

    @Test
    void webSocketConfigBeanLoaded() {
        assertThat(webSocketConfig).isNotNull();
    }
}

