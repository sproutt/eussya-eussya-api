package com.sproutt.eussyaeussyaapi.api.oauth2.service;

import com.sproutt.eussyaeussyaapi.api.oauth2.exception.OAuth2CommunicationException;
import com.sproutt.eussyaeussyaapi.api.oauth2.exception.UnSupportedOAuth2Exception;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.object.EncryptedResourceGenerator;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OAuth2RequestServiceTest {

    private String githubToken = EncryptedResourceGenerator.getGitToken();

    @Autowired
    private OAuth2RequestService oAuth2RequestService;

    @Test
    public void getUserInfo_by_github() {
        Member member = oAuth2RequestService.getUserInfo(githubToken, "github");
        Member githubMember = MemberFactory.getGithubMember();

        assertThat(member.getMemberId()).isEqualTo(githubMember.getMemberId());
        assertThat(member.getNickName()).isEqualTo(githubMember.getNickName());
    }

    @Test
    public void getUserInfo_by_wrong_provider() {
        String wrongProvider = "wrongProvider";
        assertThrows(UnSupportedOAuth2Exception.class, () -> oAuth2RequestService.getUserInfo("hello", wrongProvider));
    }

    @Test
    public void getUserInfo_by_wrong_accessToken() {
        String wrongAccessToken = "wrongwrong";

        assertThrows(OAuth2CommunicationException.class, () -> oAuth2RequestService.getUserInfo(wrongAccessToken, "github"));
    }
}