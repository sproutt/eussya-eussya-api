package com.sproutt.eussyaeussyaapi.domain.chat;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.sproutt.eussyaeussyaapi.api.chat.dto.ChatMessageResponseDTO;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member sender;

    @CreatedDate
    private LocalDateTime time;

    @Column(unique = true)
    private String message;

    public ChatMessage(ChatRoom chatRoom, Member sender, String message) {
        this.chatRoom = chatRoom;
        this.sender = sender;
        this.message = message;
        chatRoom.addChatMessage(this);
    }

    public Long getRoomId() {
        return this.chatRoom.getId();
    }

    public ChatMessageResponseDTO toDTO() {
        return ChatMessageResponseDTO.builder()
                                     .id(this.id)
                                     .sender(this.sender.getMemberId())
                                     .time(this.time)
                                     .roomId(this.chatRoom.getId())
                                     .message(this.message)
                                     .build();
    }
}
