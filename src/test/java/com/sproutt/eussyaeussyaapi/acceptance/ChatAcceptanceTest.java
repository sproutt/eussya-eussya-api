package com.sproutt.eussyaeussyaapi.acceptance;

import com.sproutt.eussyaeussyaapi.api.chat.dto.ChatMessageRequestDTO;
import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.domain.chat.ChatMessage;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompFrameHandler;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Tag("integration")
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class ChatAcceptanceTest {

    private static final String WEBSOCKET_URL = "ws://localhost:8080/ws-stomp";
    private static final String SEND_ENDPOINT = "/pub/chat/message";
    private static final String SUBSCRIBE_ENDPOINT = "/sub/chat/rooms/";
    private CompletableFuture<ChatMessage> completableFuture;

    @Autowired
    private JwtHelper jwtHelper;

    @BeforeEach
    void setUp() {
        completableFuture = new CompletableFuture<>();
    }

    @Test
    void websocketConnectTest() throws InterruptedException, ExecutionException, TimeoutException {
        Long roomId = 1l;
        String token = jwtHelper.createAccessToken(MemberFactory.getDefaultMember().toJwtInfo());
        WebSocketStompClient stompClient = new WebSocketStompClient(new SockJsClient(createTransportClient()));
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

       StompHeaders stompHeader = new StompHeaders();
       stompHeader.add("token", token);

        StompSession stompSession = stompClient.connect(WEBSOCKET_URL, new WebSocketHttpHeaders(), stompHeader, new StompSessionHandlerAdapter() {
        }).get(2, TimeUnit.SECONDS);

        stompSession.subscribe(SUBSCRIBE_ENDPOINT + roomId, new CreateStompFrameHandler());
        stompSession.send(SEND_ENDPOINT, new ChatMessageRequestDTO(token,  "test", roomId));
    }

    private List<Transport> createTransportClient() {
        List<Transport> transports = new ArrayList<>(1);
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        return transports;
    }

    private class CreateStompFrameHandler implements StompFrameHandler {
        @Override
        public Type getPayloadType(StompHeaders stompHeaders) {
            return ChatMessage.class;
        }

        @Override
        public void handleFrame(StompHeaders stompHeaders, Object o) {
            completableFuture.complete((ChatMessage) o);
        }
    }
}
