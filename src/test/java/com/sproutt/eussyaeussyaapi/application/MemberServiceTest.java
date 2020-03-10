package com.sproutt.eussyaeussyaapi.application;

import com.sproutt.eussyaeussyaapi.api.exceptions.DuplicatedMemberIdException;
import com.sproutt.eussyaeussyaapi.domain.Member;
import com.sproutt.eussyaeussyaapi.domain.MemberRepository;
import com.sproutt.eussyaeussyaapi.domain.dto.JoinDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MemberServiceTest {
    private static final String MEMBER_ID = "test@gmail.com";
    private static final String NAME = "test";
    private static final String PASSWORD = "1111";

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberRepository memberRepository;

    @Test(expected = DuplicatedMemberIdException.class)
    public void createMember_with_exist_memberId() {
        JoinDTO joinDTO = JoinDTO.builder()
                .memberId(MEMBER_ID)
                .name(NAME)
                .password(PASSWORD)
                .build();

        Member member = joinDTO.toEntity();
        when(memberRepository.findByMemberId(MEMBER_ID)).thenReturn(Optional.of(member));

        memberService.join(joinDTO);
    }
}
