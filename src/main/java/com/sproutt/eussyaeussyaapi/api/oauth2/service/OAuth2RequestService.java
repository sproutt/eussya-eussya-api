package com.sproutt.eussyaeussyaapi.api.oauth2.service;

import com.sproutt.eussyaeussyaapi.api.oauth2.dto.FacebookOAuth2UserDto;
import com.sproutt.eussyaeussyaapi.api.oauth2.dto.GithubOAuth2UserDto;
import com.sproutt.eussyaeussyaapi.api.oauth2.dto.GoogleOAuth2UserDto;
import com.sproutt.eussyaeussyaapi.api.oauth2.dto.RequestUrlDto;
import com.sproutt.eussyaeussyaapi.api.oauth2.exception.OAuth2CommunicationException;
import com.sproutt.eussyaeussyaapi.api.oauth2.exception.UnSupportedOAuth2Exception;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
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

    public Member getUserInfoByProvider(String accessToken, String provider, RequestUrlDto requestUrlDto) {
        if (requestUrlDto.isGoogle(provider)) {
            return getGoogleUserInfo(accessToken, requestUrlDto.getGoogle()).toEntity();
        }

        if (requestUrlDto.isGithub(provider)) {
            return getGithubUserInfo(accessToken, requestUrlDto.getGithub()).toEntity();
        }

        if (requestUrlDto.isFacebook(provider)) {
            return getFacebookUserInfo(accessToken, requestUrlDto.getFacebook()).toEntity();
        }

        throw new UnSupportedOAuth2Exception();
    }

    private GithubOAuth2UserDto getGithubUserInfo(String accessToken, String requestUrl) {
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

    private FacebookOAuth2UserDto getFacebookUserInfo(String accessToken, String requestUrl) {

        StringBuilder request = new StringBuilder();
        request.append(requestUrl)
               .append("?fields=id,name")
               .append("&access_token=")
               .append(accessToken);

        try {
            ResponseEntity<FacebookOAuth2UserDto> response = restTemplate
                .getForEntity(request.toString(), FacebookOAuth2UserDto.class);

            return response.getBody();
        } catch (RestClientException e) {
            throw new OAuth2CommunicationException();
        }
    }

    private GoogleOAuth2UserDto getGoogleUserInfo(String accessToken, String requestUrl) {
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
