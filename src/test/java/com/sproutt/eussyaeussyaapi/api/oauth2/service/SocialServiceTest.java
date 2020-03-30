package com.sproutt.eussyaeussyaapi.api.oauth2.service;

import com.sproutt.eussyaeussyaapi.api.oauth2.exception.UnSupportedOAuth2Exception;
import com.sproutt.eussyaeussyaapi.api.oauth2.exception.OAuth2CommunicationException;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import com.sproutt.eussyaeussyaapi.object.EncryptedResourceGenerator;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SocialServiceTest {

    private final String GITHUB = "github";
    private final String githubToken = EncryptedResourceGenerator.getGitToken();

    @Autowired
    private SocialService socialService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void init(){
        memberRepository.deleteAll();
        memberRepository.flush();
    }

    @Test
    public void login_with_no_existed_id_by_github() {

        Member githubMember = MemberFactory.getGithubMember();
        //when(oAuth2RequestService.getUserInfo(githubToken, GITHUB)).thenReturn(githubMember);

        Member member = socialService.login(githubToken, GITHUB);

        assertThat(member.getMemberId()).isEqualTo(githubMember.getMemberId());
        assertThat(member.getNickName()).isEqualTo(githubMember.getNickName());
    }

    @Test
    public void login_with_existed_id_by_github() {

        Member githubMember = MemberFactory.getGithubMember();
        memberRepository.save(githubMember);
        //when(oAuth2RequestServiceImpl.getUserInfo(githubToken, GITHUB)).thenReturn(githubMember);

        Member member = socialService.login(githubToken, GITHUB);

        assertThat(member.getMemberId()).isEqualTo(githubMember.getMemberId());
        assertThat(member.getNickName()).isEqualTo(githubMember.getNickName());
    }

    @Test
    public void login_with_wrong_token_by_github(){
        String wrongAccessToken = "wrongToken";
        //when(oAuth2RequestServiceImpl.getUserInfo(wrongAccessToken, GITHUB)).thenThrow(OAuth2CommunicationException.class);

        assertThrows(OAuth2CommunicationException.class, ()-> socialService.login(wrongAccessToken, GITHUB));
    }

    @Test
    public void login_with_wrong_provider_by_github(){
        String wrongProvider = "wrongProvider";
        //when(oAuth2RequestServiceImpl.getUserInfo(githubToken, wrongProvider)).thenThrow(NotFoundOAuth2Exception.class);

        assertThrows(UnSupportedOAuth2Exception.class, ()-> socialService.login(githubToken, wrongProvider));
    }


}