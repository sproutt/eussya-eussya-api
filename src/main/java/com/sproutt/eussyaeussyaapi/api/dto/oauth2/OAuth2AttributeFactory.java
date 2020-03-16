package com.sproutt.eussyaeussyaapi.api.dto.oauth2;

import com.sproutt.eussyaeussyaapi.domain.exceptions.NotFoundUserOAuth2;
import java.util.Map;

public class OAuth2AttributeFactory {

    public static OAuth2AttributeDto getOAuth2UserDto(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase("google")) {
            return new GoogleOAuth2AttributeDto(attributes);
        } else if (registrationId.equalsIgnoreCase("facebook")) {
            return new FacebookOAuth2AttributeDto(attributes);
        } else if (registrationId.equalsIgnoreCase("github")) {
            return new GithubOAuth2AttributeDto(attributes);
        } else {
            throw new NotFoundUserOAuth2();
        }
    }
}
