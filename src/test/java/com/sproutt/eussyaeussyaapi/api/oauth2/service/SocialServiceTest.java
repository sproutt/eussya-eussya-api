package com.sproutt.eussyaeussyaapi.api.oauth2.service;

import com.sproutt.eussyaeussyaapi.api.oauth2.exception.UnSupportedOAuth2Exception;
import com.sproutt.eussyaeussyaapi.api.oauth2.exception.OAuth2CommunicationException;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import com.sproutt.eussyaeussyaapi.object.EncryptedResourceGenerator;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class SocialServiceTest {

    private final String GITHUB = "github";
    private final String githubToken = EncryptedResourceGenerator.getGitToken();

    @Mock
    private MemberRepository memberRepository = mock(MemberRepository.class);

    private SocialService socialService;

    @BeforeEach
    public void init() {
        socialService = new SocialService(memberRepository);
    }

    @Test
    public void login() {
        Member defaultMember = MemberFactory.getDefaultMember();
        when(memberRepository.findByMemberId(defaultMember.getMemberId())).thenReturn(Optional.of(defaultMember));

        Member loginMember = socialService.login(defaultMember);

        assertThat(loginMember.getMemberId()).isEqualTo(defaultMember.getId());
        assertThat(loginMember.getNickName()).isEqualTo(defaultMember.getNickName());
    }

    @Test
    public void login_by_existed_member() {
        Member savedMember = MemberFactory.getDefaultMember();
        when(memberRepository.findByMemberId(savedMember.getMemberId())).thenReturn(Optional.of(savedMember));

        Member loginMember = socialService.login(savedMember);

        assertThat(loginMember.getMemberId()).isEqualTo(savedMember.getId());
        assertThat(loginMember.getNickName()).isEqualTo(savedMember.getNickName());
    }

}