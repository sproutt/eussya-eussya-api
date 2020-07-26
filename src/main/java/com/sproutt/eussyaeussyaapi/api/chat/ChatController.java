package com.sproutt.eussyaeussyaapi.api.chat;

import com.sproutt.eussyaeussyaapi.application.chat.ChatProducer;
import com.sproutt.eussyaeussyaapi.domain.chat.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class ChatController {

    @Autowired
    private ChatProducer chatProducer;

    @MessageMapping("/message")
    public void sendMessage(ChatMessage message) {
        System.out.println(message);
        message.setLocalDateTime(LocalDateTime.now());
        chatProducer.send("chat", message);
    }
}
