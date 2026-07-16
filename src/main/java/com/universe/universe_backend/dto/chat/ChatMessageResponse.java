// dto/chat/ChatMessageResponse.java
package com.universe.universe_backend.dto.chat;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data @AllArgsConstructor
public class ChatMessageResponse {
    private UUID id;
    private UUID conversationId;
    private UUID senderId;
    private String senderName;
    private String content;
    private OffsetDateTime createdAt;
}