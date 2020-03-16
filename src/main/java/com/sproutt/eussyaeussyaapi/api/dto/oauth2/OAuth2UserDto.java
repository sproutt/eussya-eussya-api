package com.sproutt.eussyaeussyaapi.api.dto.oauth2;

import com.sproutt.eussyaeussyaapi.domain.Member;
import java.util.Map;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public abstract class OAuth2UserDto {

    protected Map<String, Object> attributes;

    public abstract String getId();

    public abstract String getName();

    public abstract Member toEntity();

}
