package com.sproutt.eussyaeussyaapi.api.oauth2.service;

import com.sproutt.eussyaeussyaapi.api.oauth2.dto.GoogleOAuth2UserDTO;
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
public class GoogleOAuth2RequestServiceTest extends OAuth2RequestServiceTest{

    private final String GOOGLE_REQUEST_URL = "https://www.googleapis.com/oauth2/v2/userinfo";

    private OAuth2RequestService googleOAuth2RequestService;

    @BeforeEach
    void setUp() {
        googleOAuth2RequestService = new GoogleOAuth2RequestServiceImpl(restTemplate, GOOGLE_REQUEST_URL);
    }

    @DisplayName("google에서 user 정보 가져오기")
    @Test
    public void getUserInfo_by_Google() {

        GoogleOAuth2UserDTO defaultGoogleDto = DtoFactory.getGoogleOAuth2UserDto();
        Member defaultGoogleMember = MemberFactory.getGoogleMember();

        when(restTemplate
                .exchange(GOOGLE_REQUEST_URL, HttpMethod.GET, getGoogleRequest(MOCK_TOKEN), GoogleOAuth2UserDTO.class))
                .thenReturn(getResponseEntity(defaultGoogleDto));

        Member googleMember = googleOAuth2RequestService
                .getUserInfo(MOCK_TOKEN).toEntity();

        assertThat(googleMember.getMemberId()).isEqualTo(defaultGoogleMember.getMemberId());
        assertThat(googleMember.getNickName()).isEqualTo(defaultGoogleMember.getNickName());
        assertThat(googleMember.getEmail()).isEqualTo(defaultGoogleMember.getEmail());
    }

    @DisplayName("google에서 user 정보 가져오기 - accessToken이 잘못되었을 때 OAuth2CommunicationException 에러 발생")
    @Test
    public void getUserInfo_with_wrong_accessToken_by_Google() {

        when(restTemplate
                .exchange(GOOGLE_REQUEST_URL, HttpMethod.GET, getGoogleRequest(WRONG_TOKEN), GoogleOAuth2UserDTO.class))
                .thenThrow(OAuth2CommunicationException.class);

        assertThrows(OAuth2CommunicationException.class,
                () -> googleOAuth2RequestService.getUserInfo(WRONG_TOKEN));
    }

    private HttpEntity getGoogleRequest(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        return new HttpEntity(headers);
    }
}
