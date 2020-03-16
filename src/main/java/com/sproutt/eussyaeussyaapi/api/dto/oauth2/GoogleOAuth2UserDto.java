package com.sproutt.eussyaeussyaapi.api.dto.oauth2;

import com.sproutt.eussyaeussyaapi.domain.Member;
import java.util.Map;

public class GoogleOAuth2UserDto extends OAuth2UserDto {

    public GoogleOAuth2UserDto(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public Member toEntity() {

        return Member.builder()
            .password("no password")
            .memberId(getId())
            .name(getName())
            .build();
    }
}
