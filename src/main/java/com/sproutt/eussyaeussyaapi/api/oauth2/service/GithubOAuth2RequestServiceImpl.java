package com.sproutt.eussyaeussyaapi.api.oauth2.service;

import com.sproutt.eussyaeussyaapi.api.oauth2.dto.GithubOAuth2UserDTO;
import com.sproutt.eussyaeussyaapi.api.oauth2.dto.OAuth2UserInfoDTO;
import com.sproutt.eussyaeussyaapi.api.oauth2.exception.OAuth2CommunicationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
@Qualifier("github")
public class GithubOAuth2RequestServiceImpl implements OAuth2RequestService {

    private final RestTemplate restTemplate;
    private final String requestURL;

    public GithubOAuth2RequestServiceImpl(RestTemplate restTemplate, @Value("${social.github.url}") String requestURL) {
        this.restTemplate = restTemplate;
        this.requestURL = requestURL;
    }

    @Override
    public OAuth2UserInfoDTO getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + accessToken);

        HttpEntity request = new HttpEntity(headers);
        try {
            ResponseEntity<GithubOAuth2UserDTO> response = restTemplate
                    .exchange(requestURL, HttpMethod.GET, request, GithubOAuth2UserDTO.class);

            return response.getBody();

        } catch (RestClientException e) {
            throw new OAuth2CommunicationException();
        }
    }
}
