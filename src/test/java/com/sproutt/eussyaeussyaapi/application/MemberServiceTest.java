package com.sproutt.eussyaeussyaapi.application;

import com.sproutt.eussyaeussyaapi.domain.exceptions.DuplicatedMemberIdException;
import com.sproutt.eussyaeussyaapi.domain.Member;
import com.sproutt.eussyaeussyaapi.domain.MemberRepository;
import com.sproutt.eussyaeussyaapi.api.dto.JoinDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {
    private static final String MEMBER_ID = "test@gmail.com";
    private static final String NAME = "test";
    private static final String PASSWORD = "1111";

    @InjectMocks
    private MemberServiceImpl memberService;

    @Mock
    private MemberRepository memberRepository;

    @Test
    public void createMember_with_exist_memberId() {
        JoinDTO joinDTO = JoinDTO.builder()
                .memberId(MEMBER_ID)
                .name(NAME)
                .password(PASSWORD)
                .build();

        Member member = joinDTO.toEntity();
        when(memberRepository.findByMemberId(MEMBER_ID)).thenReturn(Optional.of(member));

        assertThrows(DuplicatedMemberIdException.class, () -> memberService.join(joinDTO));
    }
}
