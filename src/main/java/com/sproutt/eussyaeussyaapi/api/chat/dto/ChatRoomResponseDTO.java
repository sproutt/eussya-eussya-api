package com.sproutt.eussyaeussyaapi.api.chat.dto;

import com.sproutt.eussyaeussyaapi.domain.chat.ChatRoomType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChatRoomResponseDTO {

    private long id;
    private ChatRoomType type;

    public ChatRoomResponseDTO(Long id, ChatRoomType type) {
        this.id = id;
        this.type = type;
    }
}
