package com.sproutt.eussyaeussyaapi.api.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sproutt.eussyaeussyaapi.api.HeaderSetUpWithToken;
import com.sproutt.eussyaeussyaapi.api.chat.dto.ChatMessageResponseDTO;
import com.sproutt.eussyaeussyaapi.api.member.dto.MemberTokenCommand;
import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.application.chat.ChatService;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.domain.chat.ChatMessage;
import com.sproutt.eussyaeussyaapi.domain.chat.ChatRoom;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.NotExistMemberException;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import com.sproutt.eussyaeussyaapi.utils.exception.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ChatController.class, excludeFilters = @ComponentScan.Filter(EnableWebSecurity.class))
@EnableAspectJAutoProxy
@WithMockUser("fake_user")
public class ChatControllerTest extends HeaderSetUpWithToken {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private ChatService chatService;

    @MockBean
    private MemberService memberService;

    @MockBean
    private SimpMessageSendingOperations messagingTemplate;

    @MockBean
    private JwtHelper jwtHelper;

    private HttpHeaders headers;
    private Member loginMember;
    private Member anotherMember;

    @BeforeEach
    void setUp() {
        headers = setUpHeader();
        loginMember = MemberFactory.getDefaultMember();
        anotherMember = MemberFactory.getAnotherMember();
    }

    @Test
    @DisplayName("1:1채팅방 조회 요청 - 정상적인 경우 OK")
    void requestOneOnOneChatRoomTest_when_normal_case_then_response_ok() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ChatRoom mockChatRoom = objectMapper.readValue("{\"id\":1,\"type\":\"ONE_ON_ONE\",\"messages\":[]}", ChatRoom.class);

        when(memberService.findChatParticipantByMemberId(anotherMember.getMemberId())).thenReturn(anotherMember);
        when(memberService.findTokenOwner(any(MemberTokenCommand.class))).thenReturn(loginMember);
        when(chatService.loadOneOnOneChatRoom(eq(loginMember), eq(anotherMember))).thenReturn(mockChatRoom);

        ResultActions actions = mvc.perform(put("/chat/rooms/one-on-one?receiver=" + anotherMember.getMemberId())
                .with(csrf())
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                                   .andDo(print());

        actions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("1:1채팅방 조회 요청 - 상대방 id가 존재하지 않는 경우 Bad request")
    void requestOneOnOneChatRoomTest_when_receiverMember_not_exist_then_response_bad_request() throws Exception {
        when(memberService.findTokenOwner(any(MemberTokenCommand.class))).thenReturn(loginMember);
        when(memberService.findChatParticipantByMemberId(anotherMember.getMemberId())).thenThrow(NotExistMemberException.class);

        ResultActions actions = mvc.perform(put("/chat/rooms/one-on-one?receiver=" + anotherMember.getMemberId())
                .with(csrf())
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                                   .andDo(print());

        actions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("채팅 히스토리 조회 요청 - 정상적인 경우 OK")
    void requestChatMessageHistoryTest_when_normal_case__then_response_ok() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ChatRoom mockChatRoom = objectMapper.readValue("{\"id\":1,\"type\":\"ONE_ON_ONE\",\"messages\":[]}", ChatRoom.class);

        ChatMessage mockMessage = new ChatMessage(mockChatRoom, loginMember, "hihi");

        List<ChatMessageResponseDTO> mockChatMessages = new ArrayList<>();
        mockChatMessages.add(mockMessage.toDTO());
        mockChatRoom.addChatMessage(mockMessage);

        Page<ChatMessageResponseDTO> mockPage = new PageImpl<>(mockChatMessages, PageRequest.of(1, 20), 1);

        when(memberService.findTokenOwner(any(MemberTokenCommand.class))).thenReturn(loginMember);
        when(chatService.findChatRoom(eq(1l))).thenReturn(mockChatRoom);
        when(chatService.loadChatMessageHistory(eq(mockChatRoom), eq(loginMember), any(Pageable.class))).thenReturn(mockPage);

        ResultActions actions = mvc.perform(get("/chat/rooms/1/messages?page=1")
                .with(csrf())
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                                   .andDo(print());

        actions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("채팅 히스토리 조회 요청 - 입력받은 roomId가 존재하지 않는 경우 Bad request")
    void requestChatMessageHistoryTest_when_roomId_not_exist_then_response_bad_request() throws Exception {
        when(chatService.findChatRoom(eq(1l))).thenThrow(NoSuchElementException.class);

        ResultActions actions = mvc.perform(get("/chat/rooms/1/messages?page=1")
                .with(csrf())
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                                   .andDo(print());

        actions.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("채팅 히스토리 조회 요청 - 잘못된 데이터로 요청하는 경우(요청자가 채팅 참여자가 아닌 경우 등) Bad request")
    void requestChatMessageHistoryTest_when_request_with_wrong_data_then_response_bad_request() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        ChatRoom mockChatRoom = objectMapper.readValue("{\"id\":1,\"type\":\"ONE_ON_ONE\",\"messages\":[]}", ChatRoom.class);

        when(memberService.findTokenOwner(any(MemberTokenCommand.class))).thenReturn(loginMember);
        when(chatService.findChatRoom(eq(1l))).thenReturn(mockChatRoom);
        when(chatService.loadChatMessageHistory(eq(mockChatRoom), eq(loginMember), any(Pageable.class))).thenThrow(BadRequestException.class);

        ResultActions actions = mvc.perform(get("/chat/rooms/1/messages?page=1")
                .with(csrf())
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                                   .andDo(print());

        actions.andExpect(status().isBadRequest());
    }
}
