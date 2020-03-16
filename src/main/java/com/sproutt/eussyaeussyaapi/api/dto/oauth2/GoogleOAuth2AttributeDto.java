package com.sproutt.eussyaeussyaapi.api.dto.oauth2;

import com.sproutt.eussyaeussyaapi.domain.Member;
import java.util.Map;

public class GoogleOAuth2AttributeDto extends OAuth2AttributeDto {

    public GoogleOAuth2AttributeDto(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
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
