package com.sproutt.eussyaeussyaapi.domain.chat;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.sproutt.eussyaeussyaapi.api.chat.dto.ChatRoomResponseDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "room_id")
    private Long id;

    @Column(unique = true)
    private ChatRoomType type;

    @JsonManagedReference
    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChatMessage> messages = new ArrayList<>();

    public static ChatRoom createOneOnOne() {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.setType(ChatRoomType.ONE_ON_ONE);

        return chatRoom;
    }

    public void setType(ChatRoomType type) {
        this.type = type;
    }

    public void addChatMessage(ChatMessage chatMessage) {
        this.messages.add(chatMessage);
    }

    public ChatRoomResponseDTO toResponseDTO() {
        return new ChatRoomResponseDTO(this.id, this.type);
    }
}
