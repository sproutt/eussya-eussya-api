package com.sproutt.eussyaeussyaapi.api.oauth2.service;

import com.sproutt.eussyaeussyaapi.api.oauth2.dto.FacebookOAuth2UserDTO;
import com.sproutt.eussyaeussyaapi.api.oauth2.exception.OAuth2CommunicationException;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.object.DtoFactory;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class FacebookOAuth2RequestServiceTest {

    private final String MOCK_TOKEN = "mock";
    private final String WRONG_TOKEN = "WRONG";

    private RestTemplate restTemplate = mock(RestTemplate.class);

    private final String FACEBOOK_REQUEST_URL = "https://graph.facebook.com/v6.0/me";
    private OAuth2RequestService facebookOAuth2RequestService;

    @BeforeEach
    void setUp() {
        facebookOAuth2RequestService = new FacebookOAuth2RequestServiceImpl(restTemplate, FACEBOOK_REQUEST_URL);
    }

    @DisplayName("facebook에 user 정보 가져오기 ")
    @Test
    public void getUserInfo_by_Facebook() {

        FacebookOAuth2UserDTO defaultFacebookDTO = DtoFactory.getFacebookOAuth2UserDTO();
        Member defaultFacebookMember = MemberFactory.getFacebookMember();

        when(restTemplate.getForEntity(getFacebookRequest(MOCK_TOKEN), FacebookOAuth2UserDTO.class))
                .thenReturn(getResponseEntity(defaultFacebookDTO));

        Member facebookMember = facebookOAuth2RequestService
                .getUserInfo(MOCK_TOKEN).toEntity();

        assertThat(facebookMember.getId()).isEqualTo(defaultFacebookMember.getId());
        assertThat(facebookMember.getNickName()).isEqualTo(defaultFacebookMember.getNickName());
        assertThat(facebookMember.getEmail()).isEqualTo(defaultFacebookMember.getEmail());
    }

    @DisplayName("facebook에서 user 정보 가져오기 - accessToken이 잘못되었을 때 OAuth2CommunicationException 에러 발생")
    @Test
    public void getUserInfo_with_wrong_accessToken_by_Facebook() {

        when(restTemplate.getForEntity(getFacebookRequest(WRONG_TOKEN), FacebookOAuth2UserDTO.class))
                .thenThrow(OAuth2CommunicationException.class);

        assertThrows(OAuth2CommunicationException.class,
                () -> facebookOAuth2RequestService.getUserInfo(WRONG_TOKEN));

    }

    private String getFacebookRequest(String token) {
        StringBuilder request = new StringBuilder();
        request.append(FACEBOOK_REQUEST_URL)
               .append("?fields=id,name")
               .append("&access_token=")
               .append(token);

        return request.toString();
    }

    <T> ResponseEntity<T> getResponseEntity(T userDTO) {
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}
