package com.sproutt.eussyaeussyaapi.domain.chat;

import com.sproutt.eussyaeussyaapi.domain.member.Member;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class MemberChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "chatRoom_id")
    private ChatRoom chatRoom;

    public MemberChatRoom(Member participant, ChatRoom chatRoom) {
        this.member = participant;
        this.chatRoom = chatRoom;
    }
}
