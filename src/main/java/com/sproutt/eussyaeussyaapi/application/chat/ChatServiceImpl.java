package com.sproutt.eussyaeussyaapi.application.chat;

import com.sproutt.eussyaeussyaapi.domain.chat.*;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;

    @Override
    public List<ChatMessage> getChatHistory(Long roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow();

        return chatMessageRepository.findByRoom(room);

    }

    @Override
    @Transactional
    public ChatRoom getChatRoom(Member participant1, Member participant2) {
        Long chatRoomId = chatRoomRepository.findIdByMembers(participant1, participant2);
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElse(null);

        if (chatRoom == null) {
            chatRoom = createChatRoom(participant1, participant2);
        }
        System.out.println("roomId: " + chatRoomId);
        System.out.println("room: " + chatRoom.getId());

        return chatRoom;
    }

    @Transactional
    public ChatRoom createChatRoom(Member participant1, Member participant2) {
        ChatRoom chatRoom = new ChatRoom();

        chatRoom.addMember(participant1);
        chatRoom.addMember(participant2);
        chatRoomRepository.save(chatRoom);

        participant1.addChatRoom(chatRoom);
        participant2.addChatRoom(chatRoom);
        memberRepository.saveAll(Arrays.asList(participant1, participant1));

        return chatRoom;
    }

    @Override
    public ChatRoom findChatRoom(Long roomId) {
        return chatRoomRepository.findById(roomId).orElseThrow(RuntimeException::new);
    }
}