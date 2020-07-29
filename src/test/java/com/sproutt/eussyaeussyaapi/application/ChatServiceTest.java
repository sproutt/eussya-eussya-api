package com.sproutt.eussyaeussyaapi.application;


import com.sproutt.eussyaeussyaapi.application.chat.ChatService;
import com.sproutt.eussyaeussyaapi.domain.chat.ChatRoom;
import com.sproutt.eussyaeussyaapi.domain.chat.ChatRoomRepository;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import com.sproutt.eussyaeussyaapi.domain.member.Provider;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import com.sproutt.eussyaeussyaapi.utils.RandomGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ChatServiceTest {

    @Autowired
    private ChatService chatService;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    private Member member1;
    private Member member2;

    @BeforeEach
    void setUp() {
        member1 = MemberFactory.getDefaultMember();
        member2 = Member.builder()
                        .memberId("kjkun7631@gmail.com")
                        .nickName("bellbell")
                        .password("123abc!@")
                        .provider(Provider.LOCAL)
                        .authentication(RandomGenerator.createAuthenticationCode())
                        .build();

        memberRepository.saveAndFlush(member1);
        memberRepository.saveAndFlush(member2);
        System.out.println("chatRoomRepo size: " + chatRoomRepository.findAll().size());
        chatService.createChatRoom(member1, member2);
        chatRoomRepository.flush();
        System.out.println("chatroomRepo members: " + chatRoomRepository.findAll().get(0).getMembers());
    }

    @Test
    @Transactional
    void test() {
        System.out.println("이제 시작!");
        ChatRoom chatRoom = chatService.getChatRoom(memberRepository.findByMemberId("kjkun7631@naver.com").get(), memberRepository.findByMemberId("kjkun7631@gmail.com").get());
        System.out.println("chatRoomRepo size: " + chatRoomRepository.findAll().size());
        System.out.println("chatRoomRepo getId(): " + chatRoom.getId());
        System.out.println("chatRoomRepo getMembers(): " + chatRoom.getMembers());
    }
}
