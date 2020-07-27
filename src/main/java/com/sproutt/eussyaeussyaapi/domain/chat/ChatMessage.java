package com.sproutt.eussyaeussyaapi.domain.chat;

import com.sproutt.eussyaeussyaapi.domain.member.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class ChatMessage implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_chatMessage_room"))
    private ChatRoom room;

    private LocalDateTime localDateTime;

    @OneToOne
    private Member sender;

    @Column
    private String message;

    @Builder
    public ChatMessage(ChatRoom room, Member sender, String message, LocalDateTime localDateTime) {
        this.room = room;
        this.sender = sender;
        this.message = message;
        this.localDateTime = localDateTime;
    }
}
