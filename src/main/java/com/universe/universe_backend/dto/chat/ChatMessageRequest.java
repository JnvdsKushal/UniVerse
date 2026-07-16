// dto/chat/ChatMessageRequest.java
package com.universe.universe_backend.dto.chat;
import lombok.Data;
import java.util.UUID;

@Data
public class ChatMessageRequest {
    private UUID conversationId;
    private String content;
}