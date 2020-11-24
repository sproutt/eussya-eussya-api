package com.sproutt.eussyaeussyaapi.api.oauth2.service;

import com.sproutt.eussyaeussyaapi.api.oauth2.dto.GithubOAuth2UserDTO;
import com.sproutt.eussyaeussyaapi.api.oauth2.exception.OAuth2CommunicationException;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.object.DtoFactory;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class GithubOAuth2RequestServiceTest extends OAuth2RequestServiceTest {

    private final String GITHUB_REQUEST_URL = "https://api.github.com/user";

    private OAuth2RequestService githubOAuth2RequestService;

    @BeforeEach
    void setUp() {
        githubOAuth2RequestService = new GithubOAuth2RequestServiceImpl(restTemplate, GITHUB_REQUEST_URL);
    }

    @DisplayName("github에서 user 정보 가져오기")
    @Test
    public void getUserInfo_by_github() {

        GithubOAuth2UserDTO defaultGithubDto = DtoFactory.getGithubOAuth2UserDto();
        Member defaultGitHubMember = MemberFactory.getGithubMember();

        when(restTemplate
                .exchange(GITHUB_REQUEST_URL, HttpMethod.GET, getGithubRequest(MOCK_TOKEN), GithubOAuth2UserDTO.class))
                .thenReturn(getResponseEntity(defaultGithubDto));

        Member githubMember = githubOAuth2RequestService
                .getUserInfo(MOCK_TOKEN).toEntity();

        assertThat(githubMember.getId()).isEqualTo(defaultGitHubMember.getId());
        assertThat(githubMember.getNickName()).isEqualTo(defaultGitHubMember.getNickName());
    }

    @DisplayName("github에서 user 정보 가져오기 - accessToken이 잘못되었을 때 OAuth2CommunicationException에러 발생 ")
    @Test
    public void getUserInfo_with_wrong_accessToken_by_github() {

        when(restTemplate
                .exchange(GITHUB_REQUEST_URL, HttpMethod.GET, getGithubRequest(WRONG_TOKEN), GithubOAuth2UserDTO.class))
                .thenThrow(OAuth2CommunicationException.class);

        assertThrows(OAuth2CommunicationException.class,
                () -> githubOAuth2RequestService.getUserInfo(WRONG_TOKEN));
    }

    private HttpEntity getGithubRequest(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + token);

        return new HttpEntity(headers);
    }

}
