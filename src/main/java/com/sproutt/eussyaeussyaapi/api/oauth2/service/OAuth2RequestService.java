package com.sproutt.eussyaeussyaapi.api.oauth2.service;

import com.sproutt.eussyaeussyaapi.api.oauth2.dto.FacebookOAuth2UserDto;
import com.sproutt.eussyaeussyaapi.api.oauth2.dto.GithubOAuth2UserDto;
import com.sproutt.eussyaeussyaapi.api.oauth2.dto.GoogleOAuth2UserDto;
import com.sproutt.eussyaeussyaapi.api.oauth2.exception.OAuth2CommunicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class OAuth2RequestService {

    private final RestTemplate restTemplate;

    public GithubOAuth2UserDto getGithubUserInfo(String accessToken, String requestUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + accessToken);

        HttpEntity request = new HttpEntity(headers);
        try {
            ResponseEntity<GithubOAuth2UserDto> response = restTemplate
                .exchange(requestUrl, HttpMethod.GET, request, GithubOAuth2UserDto.class);

            return response.getBody();

        } catch (RestClientException e) {
            throw new OAuth2CommunicationException();
        }
    }

    public FacebookOAuth2UserDto getFacebookUserInfo(String accessToken, String requestUrl) {

        StringBuilder request = new StringBuilder();
        request.append(requestUrl)
               .append("?fields=id,name")
               .append("&access_token")
               .append(accessToken);

        try {
            ResponseEntity<FacebookOAuth2UserDto> response = restTemplate
                .getForEntity(request.toString(), FacebookOAuth2UserDto.class);

            return response.getBody();
        } catch (RestClientException e) {
            throw new OAuth2CommunicationException();
        }
    }

    public GoogleOAuth2UserDto getGoogleUserInfo(String accessToken, String requestUrl) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity request = new HttpEntity(headers);
        try {
            ResponseEntity<GoogleOAuth2UserDto> response = restTemplate
                .exchange(requestUrl, HttpMethod.GET, request, GoogleOAuth2UserDto.class);

            return response.getBody();

        } catch (RestClientException e) {
            throw new OAuth2CommunicationException();
        }
    }
}
