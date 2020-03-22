package com.sproutt.eussyaeussyaapi.api.oauth2.service;

import com.sproutt.eussyaeussyaapi.api.oauth2.dto.GoogleOAuth2UserDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OAuth2ServiceFactory {

    private static final Map<String, OAuth2Service> oauthServiceMapper = new HashMap<>();

    @Autowired
    public OAuth2ServiceFactory(List<OAuth2Service> services) {
        services.forEach((service) -> oauthServiceMapper.put(service.getProvider(), service));
    }

    public static OAuth2Service getOAuth2Service(String provider) {
        OAuth2Service oAuth2Service = oauthServiceMapper.get(provider);

        if (oAuth2Service == null) {
            throw new RuntimeException();
        }

        return oAuth2Service;
    }

}
