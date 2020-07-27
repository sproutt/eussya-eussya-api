package com.sproutt.eussyaeussyaapi.api.chat;

import com.sproutt.eussyaeussyaapi.api.aspect.member.LoginMember;
import com.sproutt.eussyaeussyaapi.api.chat.dto.ChatMessageDTO;
import com.sproutt.eussyaeussyaapi.api.member.dto.JwtMemberDTO;
import com.sproutt.eussyaeussyaapi.application.chat.ChatProducer;
import com.sproutt.eussyaeussyaapi.application.chat.ChatService;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.domain.chat.ChatMessage;
import com.sproutt.eussyaeussyaapi.domain.chat.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatProducer chatProducer;
    private final ChatService chatService;
    private final MemberService memberService;

    @MessageMapping("/message")
    public void sendMessage(@LoginMember JwtMemberDTO jwtMemberDTO, ChatMessageDTO messageDTO) {
        messageDTO.setLocalDateTime(LocalDateTime.now());
        ChatMessage message = ChatMessage.builder()
                                     .room(chatService.findChatRoom(messageDTO.getRoomId()))
                                     .sender(memberService.findByMemberId(jwtMemberDTO.getMemberId()))
                                     .message(messageDTO.getMessage())
                                     .localDateTime(messageDTO.getLocalDateTime())
                                     .build();

        chatProducer.send("chat", message);
    }

    @GetMapping("/chat/rooms")
    public ResponseEntity<List<ChatMessage>> readChatRoomHistory(@LoginMember JwtMemberDTO jwtMemberDTO, @RequestParam(name = "to") String memberId) {

        ChatRoom room = chatService.getChatRoom(memberService.findByMemberId(jwtMemberDTO.getMemberId()), memberService.findByMemberId(memberId));
        List<ChatMessage> history = chatService.getChatHistory(room.getId());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(history, headers, HttpStatus.OK);
    }
}
