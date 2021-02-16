package com.sproutt.eussyaeussyaapi.application.chat;

import com.sproutt.eussyaeussyaapi.domain.chat.ChatRoom;
import com.sproutt.eussyaeussyaapi.domain.chat.ChatRoomType;
import com.sproutt.eussyaeussyaapi.domain.member.Member;

import java.util.List;

public interface ChatService {
    ChatRoom loadOneOnOneChatRoom(Member participant1, Member participant2);

    List<ChatRoom> findExistedChatRooms(ChatRoomType chatRoomType, Member... participants);
}
