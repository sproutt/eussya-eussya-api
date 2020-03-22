package com.sproutt.eussyaeussyaapi.api.oauth2.service;

import com.sproutt.eussyaeussyaapi.api.oauth2.exception.UnSupportOAuth2Exception;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OAuth2ServiceFactory {

    private List<OAuth2Service> oAuth2Services;

    private static final Map<String, OAuth2Service> oauthServiceMapper = new HashMap<>();

    @Autowired
    public OAuth2ServiceFactory(List<OAuth2Service> oAuth2Services) {
        this.oAuth2Services = oAuth2Services;
    }

    @PostConstruct
    public void init() {
        oAuth2Services.forEach((service) -> oauthServiceMapper.put(service.getProvider(), service));
    }

    public static OAuth2Service getOAuth2Service(String provider) {
        OAuth2Service oAuth2Service = oauthServiceMapper.get(provider);

        if (oAuth2Service == null) {
            throw new UnSupportOAuth2Exception();
        }

        return oAuth2Service;
    }

}
