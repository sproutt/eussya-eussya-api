package com.sproutt.eussyaeussyaapi.application.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sproutt.eussyaeussyaapi.domain.chat.ChatMessage;
import com.sproutt.eussyaeussyaapi.domain.chat.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class ChatConsumer {

    private final SimpMessagingTemplate template;
    private final ChatMessageRepository chatMessageRepository;

    @KafkaListener(id = "chat-listener", topics = "chat")
    public void receive(ChatMessage message) throws Exception {
        HashMap<String, String> msg = new HashMap<>();
        msg.put("message", message.getMessage());
        msg.put("sender", message.getSender().getNickName());
        msg.put("time", message.getLocalDateTime().toString());

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(msg);

        this.template.convertAndSend("/subscribe/chat/room/" + message.getRoom(), json);
        chatMessageRepository.save(message);
    }
}
