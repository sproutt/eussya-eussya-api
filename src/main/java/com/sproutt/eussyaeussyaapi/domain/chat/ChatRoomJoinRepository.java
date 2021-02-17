package com.sproutt.eussyaeussyaapi.domain.chat;

import com.sproutt.eussyaeussyaapi.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomJoinRepository extends JpaRepository<ChatRoomJoin, Long> {

    List<ChatRoomJoin> findAllByParticipant(Member participant);

    List<ChatRoomJoin> findAllByChatRoomId(Long chatRoomId);
}
