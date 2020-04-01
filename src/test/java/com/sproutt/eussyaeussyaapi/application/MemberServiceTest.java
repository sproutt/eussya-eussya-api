package com.sproutt.eussyaeussyaapi.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sproutt.eussyaeussyaapi.api.member.EmailAuthDTO;
import com.sproutt.eussyaeussyaapi.api.member.dto.JoinDTO;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.application.member.MemberServiceImpl;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.DuplicationMemberException;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.VerificationException;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    private static final String MEMBER_ID = "kjkun7631@naver.com";
    private static final String NICKNAME = "test";
    private static final String PASSWORD = "12345aA!";

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

        Member member = MemberFactory.getDefaultMember();
        when(memberRepository.findByMemberId(MEMBER_ID)).thenReturn(Optional.of(member));

        assertThrows(DuplicationMemberException.class, () -> memberService.joinWithLocalProvider(joinDTO));
    }

    @Test
    public void authenticateEmail_with_unMatched_authCode() {
        Member member = MemberFactory.getDefaultMember();

        EmailAuthDTO emailAuthDTO = EmailAuthDTO.builder()
                                                .memberId(MEMBER_ID)
                                                .authCode("1111")
                                                .build();

        when(memberRepository.findByMemberId(MEMBER_ID)).thenReturn(Optional.of(member));

        assertThrows(VerificationException.class, () -> memberService.authenticateEmail(emailAuthDTO));
    }

    @Test
    public void checkDuplicatedMemberId_when_not_exist() {
        Member member = MemberFactory.getDefaultMember();

        when(memberRepository.findByMemberId(member.getMemberId())).thenReturn(Optional.empty());

        assertThat(memberService.isDuplicatedMemberId(member.getMemberId())).isFalse();
    }

    @Test
    public void checkDuplicatedMemberId_when_exist() {
        Member member = MemberFactory.getDefaultMember();

        when(memberRepository.findByMemberId(member.getMemberId())).thenReturn(Optional.of(member));

        assertThat(memberService.isDuplicatedMemberId(member.getMemberId())).isTrue();
    }

    @Test
    public void checkDuplicatedNickName_when_not_exist() {
        Member member = MemberFactory.getDefaultMember();

        when(memberRepository.findByNickName(member.getNickName())).thenReturn(Optional.empty());

        assertThat(memberService.isDuplicatedNickName(member.getNickName())).isFalse();
    }

    @Test
    public void checkDuplicatedNickName_when_exist() {
        Member member = MemberFactory.getDefaultMember();

        when(memberRepository.findByNickName(member.getNickName())).thenReturn(Optional.of(member));

        assertThat(memberService.isDuplicatedNickName(member.getNickName())).isTrue();
    }
}
