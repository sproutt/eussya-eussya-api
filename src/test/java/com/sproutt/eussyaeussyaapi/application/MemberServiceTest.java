package com.sproutt.eussyaeussyaapi.application;

import com.sproutt.eussyaeussyaapi.api.member.EmailAuthDTO;
import com.sproutt.eussyaeussyaapi.api.member.dto.JoinDTO;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.application.member.MemberServiceImpl;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import com.sproutt.eussyaeussyaapi.domain.member.Provider;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.DuplicationMemberException;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.VerificationException;
import com.sproutt.eussyaeussyaapi.utils.RandomGenerator;
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
    private static final String MEMBER_ID = "kjkun7631@naver.com";
    private static final String NICKNAME = "test";
    private static final String PASSWORD = "1111";

    private MemberRepository memberRepository = mock(MemberRepository.class);
    private MailService mailService = mock(MailService.class);
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberService = new MemberServiceImpl(memberRepository, mailService);
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

        assertThrows(DuplicationMemberException.class, () -> memberService.joinWithLocalProvider(joinDTO));
    }

    @Test
    public void authenticateEmail_with_unMatched_authCode() {
        Member member = defaultMember();

        EmailAuthDTO emailAuthDTO = EmailAuthDTO.builder()
                                                .memberId(MEMBER_ID)
                                                .authCode("1111")
                                                .build();

        when(memberRepository.findByMemberId(MEMBER_ID)).thenReturn(Optional.of(member));

        assertThrows(VerificationException.class, () -> memberService.authenticateEmail(emailAuthDTO));
    }

    private Member defaultMember() {
        return Member.builder()
                     .memberId(MEMBER_ID)
                     .password(PASSWORD)
                     .nickName(NICKNAME)
                     .provider(Provider.LOCAL)
                     .authentication(RandomGenerator.createAuthenticationCode())
                     .build();
    }
}
