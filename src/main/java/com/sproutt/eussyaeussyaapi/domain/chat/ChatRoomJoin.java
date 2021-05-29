package com.sproutt.eussyaeussyaapi.domain.chat;

import com.sproutt.eussyaeussyaapi.domain.member.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class ChatRoomJoin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member participant;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private ChatRoom chatRoom;

    public ChatRoomJoin(Member participant, ChatRoom chatRoom) {
        this.participant = participant;
        this.chatRoom = chatRoom;
    }
}
