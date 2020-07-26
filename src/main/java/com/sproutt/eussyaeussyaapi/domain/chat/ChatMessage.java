package com.sproutt.eussyaeussyaapi.domain.chat;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessage implements Serializable {
    private String roomId;
    private LocalDateTime localDateTime;
    private String sender;
    private String message;
}
