package com.sproutt.eussyaeussyaapi.application;

import com.sproutt.eussyaeussyaapi.api.dto.JoinDTO;
import com.sproutt.eussyaeussyaapi.domain.Member;
import com.sproutt.eussyaeussyaapi.domain.MemberRepository;
import com.sproutt.eussyaeussyaapi.domain.exceptions.DuplicatedMemberIdException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    private static final String MEMBER_ID = "test@gmail.com";
    private static final String NICKNAME = "test";
    private static final String PASSWORD = "1111";

    private MemberRepository memberRepository = mock(MemberRepository.class);
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = new MemberServiceImpl(memberRepository);
    }

    @Test
    public void createMember_with_exist_memberId() {
        JoinDTO joinDTO = JoinDTO.builder()
                .memberId(MEMBER_ID)
                .nickName(NICKNAME)
                .password(PASSWORD)
                .build();

        Member member = defaultMember();
        when(memberRepository.findByMemberId(MEMBER_ID)).thenReturn(Optional.of(member));

        assertThrows(DuplicatedMemberIdException.class, () -> memberService.join(joinDTO));
    }

    private Member defaultMember() {
        return Member.builder()
                .memberId(MEMBER_ID)
                .password(PASSWORD)
                .name(NICKNAME)
                .build();
    }
}
