package com.sproutt.eussyaeussyaapi.api.oauth2.service;

import com.sproutt.eussyaeussyaapi.api.oauth2.dto.GithubOAuth2UserDto;
import com.sproutt.eussyaeussyaapi.api.oauth2.exception.OAuth2CommunicationException;
import com.sproutt.eussyaeussyaapi.api.oauth2.exception.UnSupportedOAuth2Exception;
import com.sproutt.eussyaeussyaapi.application.MailService;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.application.member.MemberServiceImpl;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.object.DtoFactory;
import com.sproutt.eussyaeussyaapi.object.EncryptedResourceGenerator;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class OAuth2RequestServiceTest {

    private final String githubRequestUrl = "https://api.github.com/user";
    private final String githubToken = EncryptedResourceGenerator.getGitToken();

    private RestTemplate restTemplate = mock(RestTemplate.class);

    private OAuth2RequestService oAuth2RequestService;

    @BeforeEach
    void setUp() {
        oAuth2RequestService = new OAuth2RequestService(restTemplate);
    }

    @DisplayName("github에서 user 정보 가져오기")
    @Test
    public void getUserInfo_by_github() {

        GithubOAuth2UserDto defaultGithubDto = DtoFactory.getGithubOAuth2UserDto();

        ResponseEntity<GithubOAuth2UserDto> responseEntity = new ResponseEntity(defaultGithubDto, HttpStatus.OK);
        when(restTemplate.exchange(githubRequestUrl, HttpMethod.GET, getHeader(githubToken), GithubOAuth2UserDto.class))
            .thenReturn(responseEntity);

        GithubOAuth2UserDto githubOAuth2UserDto = oAuth2RequestService.getGithubUserInfo(githubToken, githubRequestUrl);

        assertThat(githubOAuth2UserDto.getId()).isEqualTo(defaultGithubDto.getId());
        assertThat(githubOAuth2UserDto.getName()).isEqualTo(defaultGithubDto.getName());
    }

    @DisplayName("github에서 user 정보 가져오기 - accessToken이 잘못되었을 때")
    @Test
    public void getUserInfo_with_wrong_accessToken_by_github() {

        String wrongToken = "wrong";

        when(restTemplate.exchange(githubRequestUrl, HttpMethod.GET, getHeader(wrongToken), GithubOAuth2UserDto.class))
            .thenThrow(OAuth2CommunicationException.class);

        assertThrows(OAuth2CommunicationException.class,
            () -> oAuth2RequestService.getGithubUserInfo(wrongToken, githubRequestUrl));
    }

    private HttpEntity getHeader(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + token);

        return new HttpEntity(headers);
    }
}