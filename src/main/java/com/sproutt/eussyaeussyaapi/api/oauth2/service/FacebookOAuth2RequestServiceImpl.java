package com.sproutt.eussyaeussyaapi.api.oauth2.service;

import com.sproutt.eussyaeussyaapi.api.oauth2.dto.FacebookOAuth2UserDTO;
import com.sproutt.eussyaeussyaapi.api.oauth2.dto.OAuth2UserInfoDTO;
import com.sproutt.eussyaeussyaapi.api.oauth2.exception.OAuth2CommunicationException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service("facebook")
public class FacebookOAuth2RequestServiceImpl implements OAuth2RequestService {

    private final RestTemplate restTemplate;
    private final String requestURL;

    public FacebookOAuth2RequestServiceImpl(RestTemplate restTemplate, @Value("${social.facebook.url}") String requestURL) {
        this.restTemplate = restTemplate;
        this.requestURL = requestURL;
    }

    @Override
    public OAuth2UserInfoDTO getUserInfo(String accessToken) {
        StringBuilder request = new StringBuilder();

        request.append(requestURL)
               .append("?fields=id,name")
               .append("&access_token=")
               .append(accessToken);

        try {
            ResponseEntity<FacebookOAuth2UserDTO> response = restTemplate
                    .getForEntity(request.toString(), FacebookOAuth2UserDTO.class);

            return response.getBody();

        } catch (RestClientException e) {

            throw new OAuth2CommunicationException();
        }
    }
}
