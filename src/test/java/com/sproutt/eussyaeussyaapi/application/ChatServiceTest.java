package com.sproutt.eussyaeussyaapi.application;

import com.sproutt.eussyaeussyaapi.application.chat.ChatService;
import com.sproutt.eussyaeussyaapi.application.chat.ChatServiceImpl;
import com.sproutt.eussyaeussyaapi.domain.chat.*;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ChatServiceTest {

    private ChatRoomRepository chatRoomRepository = mock(ChatRoomRepository.class);
    private ChatRoomJoinRepository chatRoomJoinRepository = mock(ChatRoomJoinRepository.class);
    private ChatMessageRepository chatMessageRepository = mock(ChatMessageRepository.class);
    private ChatService chatService;
    private Member loginMember;
    private Member anotherMember;

    @BeforeEach
    void setUp() {
        chatService = new ChatServiceImpl(chatRoomRepository, chatRoomJoinRepository, chatMessageRepository);
        loginMember = MemberFactory.getDefaultMember();
        anotherMember = MemberFactory.getAnotherMember();
    }

    @Test
    @DisplayName("채팅방 조회 테스트 - 입력 받은 채팅 참여자들이 모두 속해 있는 채팅방 리턴")
    void findExistedChatRoomTest_when_already_exist_ChatRoom_then_return_exist_room() {
        ChatRoom existedRoom = ChatRoom.createOneOnOne();

        ChatRoomJoin existedRoomJoinOfLoginMember = new ChatRoomJoin(loginMember, existedRoom);
        List<ChatRoomJoin> chatRoomJoinListOfLoginMember = new ArrayList<>();
        chatRoomJoinListOfLoginMember.add(existedRoomJoinOfLoginMember);

        ChatRoomJoin existedRoomJoinOfAnotherMember = new ChatRoomJoin(anotherMember, existedRoom);
        List<ChatRoomJoin> chatRoomJoinListOfAnotherMember = new ArrayList<>();
        chatRoomJoinListOfAnotherMember.add(existedRoomJoinOfAnotherMember);

        when(chatRoomJoinRepository.findAllByParticipant(loginMember)).thenReturn(chatRoomJoinListOfLoginMember);
        when(chatRoomJoinRepository.findAllByParticipant(anotherMember)).thenReturn(chatRoomJoinListOfAnotherMember);

        List<ChatRoom> savedChatRoomList = chatService.findExistedChatRooms(ChatRoomType.ONE_ON_ONE, loginMember, anotherMember);

        assertEquals(savedChatRoomList.size(), 1);
        assertEquals(savedChatRoomList.get(0), existedRoom);
        assertEquals(savedChatRoomList.get(0).getType(), ChatRoomType.ONE_ON_ONE);
    }

    @Test
    @DisplayName("채팅방 조회 테스트 - 입력 받은 채팅 참여자들이 존재하는 채팅방이 없는 경우, 리턴되는 List의 size는 0")
    void findExistedChatRoomTest_when_not_exist_ChatRoom_then_return_null() {
        ChatRoom existedRoomOfLoginMember = ChatRoom.createOneOnOne();
        ChatRoom existedRoomOfAnotherMember = ChatRoom.createOneOnOne();

        ChatRoomJoin existedRoomJoinOfLoginMember = new ChatRoomJoin(loginMember, existedRoomOfLoginMember);
        List<ChatRoomJoin> chatRoomJoinListOfLoginMember = new ArrayList<>();
        chatRoomJoinListOfLoginMember.add(existedRoomJoinOfLoginMember);

        ChatRoomJoin existedRoomJoinOfAnotherMember = new ChatRoomJoin(anotherMember, existedRoomOfAnotherMember);
        List<ChatRoomJoin> chatRoomJoinListOfAnotherMember = new ArrayList<>();
        chatRoomJoinListOfAnotherMember.add(existedRoomJoinOfAnotherMember);

        when(chatRoomJoinRepository.findAllByParticipant(loginMember)).thenReturn(chatRoomJoinListOfLoginMember);
        when(chatRoomJoinRepository.findAllByParticipant(anotherMember)).thenReturn(chatRoomJoinListOfAnotherMember);
        List<ChatRoom> savedChatRooms = chatService.findExistedChatRooms(ChatRoomType.ONE_ON_ONE, loginMember, anotherMember);

        assertEquals(savedChatRooms.size(), 0);
    }

    @Test
    @DisplayName("1:1 채팅방 생성 테스트 - 채팅 참여자들이 포함된 1:1 채팅방이 이미 존재하는 경우 기존 채팅방 리턴")
    void createOneOnOneChatRoomTest_when_already_exist_OneOnOneChatRoom_then_return_exist_room() {
        ChatRoom existedRoom = ChatRoom.createOneOnOne();

        ChatRoomJoin existedRoomJoinOfLoginMember = new ChatRoomJoin(loginMember, existedRoom);
        List<ChatRoomJoin> chatRoomJoinListOfLoginMember = new ArrayList<>();
        chatRoomJoinListOfLoginMember.add(existedRoomJoinOfLoginMember);

        ChatRoomJoin existedRoomJoinOfAnotherMember = new ChatRoomJoin(anotherMember, existedRoom);
        List<ChatRoomJoin> chatRoomJoinListOfAnotherMember = new ArrayList<>();
        chatRoomJoinListOfAnotherMember.add(existedRoomJoinOfAnotherMember);

        when(chatRoomJoinRepository.findAllByParticipant(loginMember)).thenReturn(chatRoomJoinListOfLoginMember);
        when(chatRoomJoinRepository.findAllByParticipant(anotherMember)).thenReturn(chatRoomJoinListOfAnotherMember);

        ChatRoom savedChatRoom = chatService.loadOneOnOneChatRoom(loginMember, anotherMember);

        assertEquals(savedChatRoom, existedRoom);
        assertEquals(savedChatRoom.getType(), ChatRoomType.ONE_ON_ONE);
    }

    @Test
    @DisplayName("1:1 채팅방 조회 테스트 - 조회 혹은 생성될 1:1 채팅방의 두 참여자가 동일한 회원이어서는 안됨")
    void findOneOnOneChatRoomTest_when_OneOnOneChatRoom_participants_are_same_Member_then_return_fail() {
        Exception exception = assertThrows(RuntimeException.class, () -> chatService.loadOneOnOneChatRoom(loginMember, loginMember));
        assertEquals("participant1 is equals participant2", exception.getMessage());
    }

    @Test
    @DisplayName("1:1 채팅방 조회 테스트 - 채팅 참여자들 포함된 1:1 채팅방이 두 개 이상이어서는 안됨")
    void findOneOnOneChatRoomTest_when_exist_OneOnOneChatRoom_2_or_more_then_return_fail() {
        ChatRoom existedRoom1 = ChatRoom.createOneOnOne();
        ChatRoom existedRoom2 = ChatRoom.createOneOnOne();

        List<ChatRoomJoin> chatRoomJoinListOfLoginMember = new ArrayList<>();
        chatRoomJoinListOfLoginMember.add(new ChatRoomJoin(loginMember, existedRoom1));
        chatRoomJoinListOfLoginMember.add(new ChatRoomJoin(loginMember, existedRoom2));

        List<ChatRoomJoin> chatRoomJoinListOfAnotherMember = new ArrayList<>();
        chatRoomJoinListOfAnotherMember.add(new ChatRoomJoin(loginMember, existedRoom1));
        chatRoomJoinListOfAnotherMember.add(new ChatRoomJoin(loginMember, existedRoom2));

        when(chatRoomJoinRepository.findAllByParticipant(loginMember)).thenReturn(chatRoomJoinListOfLoginMember);
        when(chatRoomJoinRepository.findAllByParticipant(anotherMember)).thenReturn(chatRoomJoinListOfAnotherMember);

        Exception exception = assertThrows(RuntimeException.class, () -> chatService.loadOneOnOneChatRoom(loginMember, anotherMember));
        assertEquals("채팅 참여자들 포함된 1:1 채팅방이 두 개 이상이어서는 안됨", exception.getMessage());
    }

    @Test
    @DisplayName("1:1 채팅방 생성 테스트 - 채팅 참여자들 포함된 1:1 채팅방이 존재하지 않는 경우 새로 생성")
    void createOneOnOneChatRoomTest() {
        ChatRoom chatRoomOnlyLoginMember = ChatRoom.createOneOnOne();
        ChatRoomJoin existedRoomJoinOfLoginMember = new ChatRoomJoin(loginMember, chatRoomOnlyLoginMember);
        List<ChatRoomJoin> chatRoomJoinListOfLoginMember = new ArrayList<>();
        chatRoomJoinListOfLoginMember.add(existedRoomJoinOfLoginMember);

        ChatRoom chatRoomOnlyAnotherMember = ChatRoom.createOneOnOne();
        ChatRoomJoin existedRoomJoinOfAnotherMember = new ChatRoomJoin(anotherMember, chatRoomOnlyAnotherMember);
        List<ChatRoomJoin> chatRoomJoinListOfAnotherMember = new ArrayList<>();
        chatRoomJoinListOfAnotherMember.add(existedRoomJoinOfAnotherMember);

        when(chatRoomJoinRepository.findAllByParticipant(loginMember)).thenReturn(chatRoomJoinListOfLoginMember);
        when(chatRoomJoinRepository.findAllByParticipant(anotherMember)).thenReturn(chatRoomJoinListOfAnotherMember);
        when(chatRoomRepository.save(any(ChatRoom.class))).thenReturn(ChatRoom.createOneOnOne());

        ChatRoom savedChatRoom = chatService.loadOneOnOneChatRoom(loginMember, anotherMember);

        assertNotEquals(savedChatRoom, chatRoomOnlyLoginMember);
        assertNotEquals(savedChatRoom, chatRoomOnlyAnotherMember);
        assertEquals(savedChatRoom.getType(), ChatRoomType.ONE_ON_ONE);
    }
}