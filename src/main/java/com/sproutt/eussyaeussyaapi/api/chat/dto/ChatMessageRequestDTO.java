package com.sproutt.eussyaeussyaapi.api.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatMessageRequestDTO {

    private String token;
    private String message;
    private Long roomId;

    @Builder
    public ChatMessageRequestDTO(String token, String message, Long roomId) {
        this.token = token;
        this.message = message;
        this.roomId = roomId;
    }
}
