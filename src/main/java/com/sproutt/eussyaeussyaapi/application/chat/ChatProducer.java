package com.sproutt.eussyaeussyaapi.application.chat;

import com.sproutt.eussyaeussyaapi.domain.chat.ChatMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class ChatProducer {

    @Autowired
    private KafkaTemplate<String, ChatMessage> kafkaTemplate;

    public void send(String topic, ChatMessage data) {
        kafkaTemplate.send(topic, data);
    }
}
