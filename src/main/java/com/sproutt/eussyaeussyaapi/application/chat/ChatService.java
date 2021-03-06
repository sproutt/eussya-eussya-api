package com.sproutt.eussyaeussyaapi.application.chat;

import com.sproutt.eussyaeussyaapi.api.chat.dto.ChatMessageResponseDTO;
import com.sproutt.eussyaeussyaapi.domain.chat.ChatMessage;
import com.sproutt.eussyaeussyaapi.domain.chat.ChatRoom;
import com.sproutt.eussyaeussyaapi.domain.chat.ChatRoomType;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Set;

public interface ChatService {
    ChatRoom loadOneOnOneChatRoom(Member participant1, Member participant2);

    List<ChatRoom> findExistedChatRooms(ChatRoomType chatRoomType, Member... participants);

    Page<ChatMessageResponseDTO> loadChatMessageHistory(ChatRoom chatRoom, Member loginMember, Pageable pageable);

    Set<Member> findChatRoomParticipants(Long chatRoomId);

    ChatMessage saveChatMessage(Long chatRoomId, Member Sender, String message);

    ChatRoom findChatRoom(Long chatRoomId);
}
