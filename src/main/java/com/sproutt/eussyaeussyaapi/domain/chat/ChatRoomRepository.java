package com.sproutt.eussyaeussyaapi.domain.chat;

import com.sproutt.eussyaeussyaapi.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query(value = "SELECT mc.chatroom_id " +
            "FROM Member_ChatRoom As mc " +
            "INNER JOIN Member As m On mc.member_id = m.id " +
            "INNER JOIN chat_room AS c ON mc.chatroom_id = c.id " +
            "WHERE mc.member_id In (:participant1, :participant2)", nativeQuery = true)
    Long findIdByMembers(@Param("participant1") Member participant1, @Param("participant2") Member participant2);
}
