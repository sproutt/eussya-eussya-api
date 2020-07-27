package com.sproutt.eussyaeussyaapi.application.chat;

import com.sproutt.eussyaeussyaapi.domain.chat.*;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberChatRoomRepository memberChatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Override
    public List<ChatMessage> getChatHistory(Long roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId).orElseGet(() -> chatRoomRepository.save(new ChatRoom()));

        return room.getChatMessageList();
    }

    @Override
    public ChatRoom getChatRoom(Member participant1, Member participant2) {
        List<MemberChatRoom> memberChatRoomsWithParticipant1 = memberChatRoomRepository.findByMember(participant1);
        List<MemberChatRoom> memberChatRoomsWithParticipant2 = memberChatRoomRepository.findByMember(participant2);

        for (MemberChatRoom memberChatRoomWith1 : memberChatRoomsWithParticipant1) {
            for (MemberChatRoom memberChatRoomWith2 : memberChatRoomsWithParticipant2) {
                if (memberChatRoomWith1.getChatRoom().equals(memberChatRoomWith2.getChatRoom())) {
                    return memberChatRoomWith1.getChatRoom();
                }
            }
        }

        ChatRoom chatRoom = chatRoomRepository.save(new ChatRoom());
        MemberChatRoom memberChatRoom1 = new MemberChatRoom(participant1, chatRoom);
        MemberChatRoom memberChatRoom2 = new MemberChatRoom(participant2, chatRoom);

        memberChatRoomRepository.save(memberChatRoom1);
        memberChatRoomRepository.save(memberChatRoom2);

        return chatRoom;
    }

    @Override
    public ChatRoom findChatRoom(Long roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow(RuntimeException::new);
    }
}
