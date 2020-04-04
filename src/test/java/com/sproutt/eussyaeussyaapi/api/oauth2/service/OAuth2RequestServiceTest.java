package com.sproutt.eussyaeussyaapi.api.oauth2.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.sproutt.eussyaeussyaapi.api.oauth2.dto.FacebookOAuth2UserDto;
import com.sproutt.eussyaeussyaapi.api.oauth2.dto.GithubOAuth2UserDto;
import com.sproutt.eussyaeussyaapi.api.oauth2.dto.GoogleOAuth2UserDto;
import com.sproutt.eussyaeussyaapi.api.oauth2.exception.OAuth2CommunicationException;
import com.sproutt.eussyaeussyaapi.api.oauth2.exception.UnSupportedOAuth2Exception;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
class OAuth2RequestServiceTest {

    private final String GITHUB_REQUEST_URL = "https://api.github.com/user";
    private final String FACEBOOK_REQUEST_URL = "https://graph.facebook.com/v6.0/me";
    private final String GOOGLE_REQUEST_URL = "https://www.googleapis.com/oauth2/v2/userinfo";
    private final String MOCK_TOKEN = "mock";
    private final String WRONG_TOKEN = "WRONG";

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
        Member defaultGitHubMember = MemberFactory.getGithubMember();

        when(restTemplate
            .exchange(GITHUB_REQUEST_URL, HttpMethod.GET, getGithubRequest(MOCK_TOKEN), GithubOAuth2UserDto.class))
            .thenReturn(getResponseEntity(defaultGithubDto));

        Member githubMember = oAuth2RequestService
            .getUserInfoByProvider(MOCK_TOKEN, "github", DtoFactory.getRequestUrlDto());

        assertThat(githubMember.getId()).isEqualTo(defaultGitHubMember.getId());
        assertThat(githubMember.getNickName()).isEqualTo(defaultGitHubMember.getNickName());
    }

    @DisplayName("github에서 user 정보 가져오기 - accessToken이 잘못되었을 때")
    @Test
    public void getUserInfo_with_wrong_accessToken_by_github() {

        when(restTemplate
            .exchange(GITHUB_REQUEST_URL, HttpMethod.GET, getGithubRequest(WRONG_TOKEN), GithubOAuth2UserDto.class))
            .thenThrow(OAuth2CommunicationException.class);

        assertThrows(OAuth2CommunicationException.class,
            () -> oAuth2RequestService.getUserInfoByProvider(WRONG_TOKEN, "github", DtoFactory.getRequestUrlDto()));
    }

    @DisplayName("facebook에 user 정보 가져오기 ")
    @Test
    public void getUserInfo_by_Facebook() {

        FacebookOAuth2UserDto defaultFacebookDto = DtoFactory.getFacebookOAuth2UserDto();
        Member defaultFacebookMember = MemberFactory.getFacebookMember();

        when(restTemplate.getForEntity(getFacebookRequest(MOCK_TOKEN), FacebookOAuth2UserDto.class))
            .thenReturn(getResponseEntity(defaultFacebookDto));

        Member facebookMember = oAuth2RequestService
            .getUserInfoByProvider(MOCK_TOKEN, "facebook", DtoFactory.getRequestUrlDto());

        assertThat(facebookMember.getId()).isEqualTo(defaultFacebookMember.getId());
        assertThat(facebookMember.getNickName()).isEqualTo(defaultFacebookMember.getNickName());
        assertThat(facebookMember.getEmail()).isEqualTo(defaultFacebookMember.getEmail());
    }

    @DisplayName("facebook에서 user 정보 가져오기 - accessToken이 잘못되었을 때")
    @Test
    public void getUserInfo_with_wrong_accessToken_by_Facebook() {

        when(restTemplate.getForEntity(getFacebookRequest(WRONG_TOKEN), FacebookOAuth2UserDto.class))
            .thenThrow(OAuth2CommunicationException.class);

        assertThrows(OAuth2CommunicationException.class,
            () -> oAuth2RequestService.getUserInfoByProvider(WRONG_TOKEN, "facebook", DtoFactory.getRequestUrlDto()));

    }

    @DisplayName("google에서 user 정보 가져오기")
    @Test
    public void getUserInfo_by_Google() {

        GoogleOAuth2UserDto defaultGoogleDto = DtoFactory.getGoogleOAuth2UserDto();
        Member defaultGoogleMember = MemberFactory.getGoogleMember();

        when(restTemplate
            .exchange(GOOGLE_REQUEST_URL, HttpMethod.GET, getGoogleRequest(MOCK_TOKEN), GoogleOAuth2UserDto.class))
            .thenReturn(getResponseEntity(defaultGoogleDto));

        Member googleMember = oAuth2RequestService
            .getUserInfoByProvider(MOCK_TOKEN, "google", DtoFactory.getRequestUrlDto());

        assertThat(googleMember.getId()).isEqualTo(defaultGoogleMember.getId());
        assertThat(googleMember.getNickName()).isEqualTo(defaultGoogleMember.getNickName());
        assertThat(googleMember.getEmail()).isEqualTo(defaultGoogleMember.getEmail());
    }

    @DisplayName("google에서 user 정보 가져오기 - accessToken이 잘못되었을 때")
    @Test
    public void getUserInfo_with_wrong_accessToken_by_Google() {

        when(restTemplate
            .exchange(GOOGLE_REQUEST_URL, HttpMethod.GET, getGoogleRequest(WRONG_TOKEN), GoogleOAuth2UserDto.class))
            .thenThrow(OAuth2CommunicationException.class);

        assertThrows(OAuth2CommunicationException.class,
            () -> oAuth2RequestService.getUserInfoByProvider(WRONG_TOKEN, "google", DtoFactory.getRequestUrlDto()));
    }

    @DisplayName("지원하지 않는 social login일 때")
    @Test
    public void getUserInfo_by_strange_Provider() {
        assertThrows(UnSupportedOAuth2Exception.class, () -> oAuth2RequestService
            .getUserInfoByProvider("AnyToken", "worngProvider", DtoFactory.getRequestUrlDto()));
    }

    private HttpEntity getGithubRequest(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + token);

        return new HttpEntity(headers);
    }

    private String getFacebookRequest(String token) {
        StringBuilder request = new StringBuilder();
        request.append(FACEBOOK_REQUEST_URL)
               .append("?fields=id,name")
               .append("&access_token=")
               .append(token);

        return request.toString();
    }

    private HttpEntity getGoogleRequest(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        return new HttpEntity(headers);
    }

    private <T> ResponseEntity<T> getResponseEntity(T userDto) {
        return new ResponseEntity(userDto, HttpStatus.OK);
    }
}