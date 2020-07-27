package com.sproutt.eussyaeussyaapi.application.chat;

import com.sproutt.eussyaeussyaapi.domain.chat.ChatMessage;
import com.sproutt.eussyaeussyaapi.domain.chat.ChatRoom;
import com.sproutt.eussyaeussyaapi.domain.member.Member;

import java.util.List;

public interface ChatService {
    List<ChatMessage> getChatHistory(Long roomId);

    ChatRoom getChatRoom(Member participant1, Member participant2);

    ChatRoom findChatRoom(Long roomId);
}
