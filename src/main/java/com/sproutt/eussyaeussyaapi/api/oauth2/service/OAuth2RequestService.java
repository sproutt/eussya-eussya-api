package com.sproutt.eussyaeussyaapi.api.oauth2.service;

import com.sproutt.eussyaeussyaapi.api.oauth2.dto.FacebookOAuth2UserDto;
import com.sproutt.eussyaeussyaapi.api.oauth2.dto.GithubOAuth2UserDto;
import com.sproutt.eussyaeussyaapi.api.oauth2.dto.GoogleOAuth2UserDto;
import com.sproutt.eussyaeussyaapi.api.oauth2.exception.OAuth2CommunicationException;
import com.sproutt.eussyaeussyaapi.api.oauth2.exception.UnSupportedOAuth2Exception;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${social.github.url}")
    private String githubRequestUrl;

    @Value("${social.google.url}")
    private String googleRequestUrl;

    @Value("${social.facebook.url}")
    private String facebookRequestUrl;

    public Member getUserInfo(String accessToken, String provider) {
        if (provider.equals("github")) {
            return getGithubUserInfo(accessToken).toEntity();
        }

        if (provider.equals("google")) {
            return getGoogleUserInfo(accessToken).toEntity();
        }

        if (provider.equals("facebook")) {
            return getFacebookUserInfo(accessToken).toEntity();
        }

        throw new UnSupportedOAuth2Exception();
    }

    private GithubOAuth2UserDto getGithubUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + accessToken);

        HttpEntity request = new HttpEntity(headers);
        try {
            ResponseEntity<GithubOAuth2UserDto> response = restTemplate
                    .exchange(githubRequestUrl, HttpMethod.GET, request, GithubOAuth2UserDto.class);

            return response.getBody();

        } catch (RestClientException e) {
            throw new OAuth2CommunicationException();
        }
    }

    private FacebookOAuth2UserDto getFacebookUserInfo(String accessToken) {

        StringBuilder request = new StringBuilder();
        request.append(facebookRequestUrl)
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

    private GoogleOAuth2UserDto getGoogleUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity request = new HttpEntity(headers);
        try {
            ResponseEntity<GoogleOAuth2UserDto> response = restTemplate
                    .exchange(googleRequestUrl, HttpMethod.GET, request, GoogleOAuth2UserDto.class);

            return response.getBody();

        } catch (RestClientException e) {
            throw new OAuth2CommunicationException();
        }
    }
}
