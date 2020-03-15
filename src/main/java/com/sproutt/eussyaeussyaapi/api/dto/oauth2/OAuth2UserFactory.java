package com.sproutt.eussyaeussyaapi.api.dto.oauth2;

import com.sproutt.eussyaeussyaapi.domain.exceptions.NotFoundUserOAuth2;
import java.util.Map;

public class OAuth2UserFactory {

    public static OAuth2UserDto getOAuth2UserDto(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase("github")) {
            return new GithubOAuth2UserDto(attributes);
        } else {
            throw new NotFoundUserOAuth2();
        }
    }
}
