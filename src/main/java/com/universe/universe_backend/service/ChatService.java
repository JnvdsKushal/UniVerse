package com.universe.universe_backend.service;

import com.universe.universe_backend.dto.chat.ChatMessageRequest;
import com.universe.universe_backend.dto.chat.ChatMessageResponse;
import com.universe.universe_backend.entity.Conversation;
import com.universe.universe_backend.entity.Message;
import com.universe.universe_backend.entity.User;
import com.universe.universe_backend.repository.ConversationRepository;
import com.universe.universe_backend.repository.MessageRepository;
import com.universe.universe_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public Conversation getOrCreateConversation(UUID userAId, UUID userBId) {
        return conversationRepository.findBetween(userAId, userBId)
                .orElseGet(() -> {
                    User a = userRepository.findById(userAId).orElseThrow();
                    User b = userRepository.findById(userBId).orElseThrow();
                    Conversation c = Conversation.builder().userOne(a).userTwo(b).build();
                    return conversationRepository.save(c);
                });
    }

    public ChatMessageResponse saveMessage(String senderEmail, ChatMessageRequest req) {
        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Conversation conversation = conversationRepository.findById(req.getConversationId())
                .orElseThrow(() -> new IllegalArgumentException("Conversation not found"));

        Message message = Message.builder()
                .conversation(conversation)
                .sender(sender)
                .content(req.getContent())
                .build();

        return toResponse(messageRepository.save(message));
    }

    public List<ChatMessageResponse> getHistory(UUID conversationId) {
        return messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    private ChatMessageResponse toResponse(Message m) {
        return new ChatMessageResponse(
                m.getId(), m.getConversation().getId(), m.getSender().getId(),
                m.getSender().getFullName(), m.getContent(), m.getCreatedAt()
        );
    }
}