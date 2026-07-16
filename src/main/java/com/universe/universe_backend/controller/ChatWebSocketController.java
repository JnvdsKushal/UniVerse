package com.universe.universe_backend.controller;

import com.universe.universe_backend.dto.chat.ChatMessageRequest;
import com.universe.universe_backend.dto.chat.ChatMessageResponse;
import com.universe.universe_backend.entity.Conversation;
import com.universe.universe_backend.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void send(ChatMessageRequest request) {
        String senderEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        ChatMessageResponse saved = chatService.saveMessage(senderEmail, request);
        messagingTemplate.convertAndSend("/topic/conversation." + request.getConversationId(), saved);
    }
}

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
class ChatRestController {

    private final ChatService chatService;

    @PostMapping("/conversations")
    public ResponseEntity<Conversation> startConversation(@RequestParam UUID otherUserId, @AuthenticationPrincipal String email) {
        // NOTE: quick simplification — resolving current user's id from email happens inside a real
        // implementation via UserRepository; wire this the same way other controllers do if needed.
        return ResponseEntity.ok(null); // placeholder — see note below
    }

    @GetMapping("/conversations/{id}/messages")
    public ResponseEntity<List<ChatMessageResponse>> history(@PathVariable UUID id) {
        return ResponseEntity.ok(chatService.getHistory(id));
    }
}