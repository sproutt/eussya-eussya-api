package com.sproutt.eussyaeussyaapi.api.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatMessageResponseDTO {

    private Long id;
    private String sender;
    private LocalDateTime time;
    private String message;
    private Long roomId;

    @Builder
    public ChatMessageResponseDTO(Long id, String sender, LocalDateTime time, String message, Long roomId) {
        this.id = id;
        this.sender = sender;
        this.time = time;
        this.message = message;
        this.roomId = roomId;
    }
}
