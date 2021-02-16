package com.sproutt.eussyaeussyaapi.application.chat;

import com.sproutt.eussyaeussyaapi.domain.chat.*;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChatServiceImpl implements ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomJoinRepository chatRoomJoinRepository;
    private final ChatMessageRepository chatMessageRepository;

    public ChatServiceImpl(ChatRoomRepository chatRoomRepository, ChatRoomJoinRepository chatRoomJoinRepository, ChatMessageRepository chatMessageRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatRoomJoinRepository = chatRoomJoinRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    @Transactional
    @Override
    public ChatRoom loadOneOnOneChatRoom(Member participant1, Member participant2) {
        // 2명의 참가자가 동일한 회원인 경우 예외
        if (participant1.isSame(participant2)) {
            throw new RuntimeException("participant1 is equals participant2");
        }

        List<ChatRoom> existedChatRooms = findExistedChatRooms(ChatRoomType.ONE_ON_ONE, participant1, participant2);
        if (existedChatRooms.size() == 1) {
            return existedChatRooms.get(0);
        }

        // 1:1 채팅을 조회하는데 두 사용자가 들어가있는 1대1 채팅방이 2개 이상이다??
        if (existedChatRooms.size() > 1) {
            throw new RuntimeException("채팅 참여자들 포함된 1:1 채팅방이 두 개 이상이어서는 안됨");
        }

        ChatRoom chatRoom = chatRoomRepository.save(ChatRoom.createOneOnOne());
        chatRoomJoinRepository.save(new ChatRoomJoin(participant1, chatRoom));
        chatRoomJoinRepository.save(new ChatRoomJoin(participant2, chatRoom));

        return chatRoom;
    }

    @Override
    public List<ChatRoom> findExistedChatRooms(ChatRoomType chatRoomType, Member... participants) {
        List<List<ChatRoomJoin>> chatRoomJoinList = new ArrayList<>();
        Arrays.stream(participants).forEach(participant -> chatRoomJoinList.add(chatRoomJoinRepository.findAllByParticipant(participant).stream()
                                                                                                      .filter(chatRoomJoin -> chatRoomJoin.getChatRoom()
                                                                                                                                          .getType().equals(chatRoomType))
                                                                                                      .collect(Collectors.toList())));

        Map<ChatRoom, Integer> intersectionCounter = setIntersectionCounter(chatRoomJoinList);
        List<ChatRoom> chatRoomList = new ArrayList<>();

        for (ChatRoom chatRoom : intersectionCounter.keySet()) {
            if (intersectionCounter.get(chatRoom) == participants.length) {
                chatRoomList.add(chatRoom);
            }
        }

        return chatRoomList;
    }

    private Map<ChatRoom, Integer> setIntersectionCounter(List<List<ChatRoomJoin>> chatRoomJoinList) {
        Map<ChatRoom, Integer> intersectionCounter = new HashMap<>();
        for (List<ChatRoomJoin> list : chatRoomJoinList) {
            for (ChatRoomJoin chatRoomJoin : list) {
                intersectionCounter.put(chatRoomJoin.getChatRoom(), intersectionCounter.getOrDefault(chatRoomJoin.getChatRoom(), 0) + 1);
            }
        }

        return intersectionCounter;
    }
}
