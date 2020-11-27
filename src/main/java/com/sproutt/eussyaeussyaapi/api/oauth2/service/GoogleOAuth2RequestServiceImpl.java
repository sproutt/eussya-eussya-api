package com.sproutt.eussyaeussyaapi.api.oauth2.service;

import com.sproutt.eussyaeussyaapi.api.oauth2.dto.GoogleOAuth2UserDTO;
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

@Service("google")
public class GoogleOAuth2RequestServiceImpl implements OAuth2RequestService {

    private final RestTemplate restTemplate;
    private final String requestURL;

    public GoogleOAuth2RequestServiceImpl(RestTemplate restTemplate, @Value("${social.google.url}") String requestURL) {
        this.restTemplate = restTemplate;
        this.requestURL = requestURL;
    }

    @Override
    public OAuth2UserInfoDTO getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity request = new HttpEntity(headers);

        try {
            ResponseEntity<GoogleOAuth2UserDTO> response = restTemplate
                    .exchange(requestURL, HttpMethod.GET, request, GoogleOAuth2UserDTO.class);

            return response.getBody();

        } catch (RestClientException e) {

            throw new OAuth2CommunicationException();
        }
    }
}
