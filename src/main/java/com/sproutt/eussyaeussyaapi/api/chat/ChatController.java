package com.sproutt.eussyaeussyaapi.api.chat;

import com.sproutt.eussyaeussyaapi.api.aspect.member.LoginMember;
import com.sproutt.eussyaeussyaapi.api.chat.dto.ChatMessageRequestDTO;
import com.sproutt.eussyaeussyaapi.api.chat.dto.ChatMessageResponseDTO;
import com.sproutt.eussyaeussyaapi.api.chat.dto.ChatRoomResponseDTO;
import com.sproutt.eussyaeussyaapi.api.member.dto.MemberTokenCommand;
import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.application.chat.ChatService;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.domain.chat.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChatController {

    private final SimpMessageSendingOperations messagingTemplate;
    private final ChatService chatService;
    private final MemberService memberService;
    private final JwtHelper jwtHelper;

    public ChatController(SimpMessageSendingOperations messagingTemplate, ChatService chatService, MemberService memberService, JwtHelper jwtHelper) {
        this.messagingTemplate = messagingTemplate;
        this.chatService = chatService;
        this.memberService = memberService;
        this.jwtHelper = jwtHelper;
    }

    @MessageMapping("/chat/message")
    public void message(@Payload ChatMessageRequestDTO message) {
        chatService.saveChatMessage(
                message.getRoomId(),
                memberService.findTokenOwner(jwtHelper.decryptToken(message.getToken())),
                message.getMessage());

        messagingTemplate.convertAndSend("/sub/chat/rooms/" + message.getRoomId(), message);
    }

    @PutMapping("/chat/rooms/one-on-one")
    public ResponseEntity<ChatRoomResponseDTO> requestOneOnOneChatRoom(@LoginMember MemberTokenCommand memberTokenCommand,
                                                                       @RequestParam(name = "receiver", required = true) String receiverMemberId) {
        ChatRoom chatRoom = chatService.loadOneOnOneChatRoom(
                memberService.findTokenOwner(memberTokenCommand),
                memberService.findChatParticipantByMemberId(receiverMemberId));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(chatRoom.toResponseDTO(), headers, HttpStatus.OK);
    }

    @GetMapping("/chat/rooms/{chatRoomId}/messages")
    public ResponseEntity<Page<ChatMessageResponseDTO>> requestChatMessageHistory(@PathVariable Long chatRoomId,
                                                                                  @LoginMember MemberTokenCommand memberTokenCommand,
                                                                                  @RequestParam(name = "page") int page) {
        ChatRoom chatRoom = chatService.findChatRoom(chatRoomId);

        Pageable pageable = PageRequest.of(page, 30, Sort.by("time").descending());
        Page<ChatMessageResponseDTO> history = chatService.loadChatMessageHistory(chatRoom, memberService.findTokenOwner(memberTokenCommand), pageable);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(history, headers, HttpStatus.OK);
    }
}
