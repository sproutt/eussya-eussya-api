package com.sproutt.eussyaeussyaapi.api.config.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);


    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection");

    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        System.out.println("disconnected");
//        MemberDTO memberDTO = MemberDTO.builder()
//                                       .id((long) headerAccessor.getSessionAttributes().get("id"))
//                                       .nickName((String) headerAccessor.getSessionAttributes().get("nickname"))
//                                       .memberId((String) headerAccessor.getSessionAttributes().get("memberId"))
//                                       .build();
//        memberDTO.disConnect();
//
//        connectMemberProducer.send("/publish/members", memberDTO);
    }
}