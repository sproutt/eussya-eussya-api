package com.sproutt.eussyaeussyaapi.api.oauth2.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import com.sproutt.eussyaeussyaapi.object.EncryptedResourceGenerator;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class SocialServiceTest {

    @Mock
    private MemberRepository memberRepository = mock(MemberRepository.class);

    private SocialService socialService;

    @BeforeEach
    public void init() {
        socialService = new SocialService(memberRepository);
    }

    @Test
    public void login() {
        Member defaultMember = MemberFactory.getGithubMember();
        when(memberRepository.existsByMemberId(defaultMember.getMemberId())).thenReturn(false);
        when(memberRepository.save(defaultMember)).thenReturn(defaultMember);

        Member loginMember = socialService.login(defaultMember);

        assertThat(loginMember.getMemberId()).isEqualTo(defaultMember.getMemberId());
        assertThat(loginMember.getNickName()).isEqualTo(defaultMember.getNickName());
    }

    @Test
    public void login_by_existed_member() {
        Member savedMember = MemberFactory.getGithubMember();
        when(memberRepository.existsByMemberId(savedMember.getMemberId())).thenReturn(true);
        when(memberRepository.findByMemberId(savedMember.getMemberId())).thenReturn(Optional.of(savedMember));

        Member loginMember = socialService.login(savedMember);

        assertThat(loginMember.getMemberId()).isEqualTo(savedMember.getMemberId());
        assertThat(loginMember.getNickName()).isEqualTo(savedMember.getNickName());
    }

}