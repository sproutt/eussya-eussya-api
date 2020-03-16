package com.sproutt.eussyaeussyaapi.api.dto.oauth2;

import com.sproutt.eussyaeussyaapi.domain.Member;
import java.util.Map;

public class GithubOAuth2UserDto extends OAuth2UserDto {

    public GithubOAuth2UserDto(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return ((Integer) attributes.get("id")).toString();
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
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
