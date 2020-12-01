package com.sproutt.eussyaeussyaapi.api.oauth2.dto;

import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.Provider;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class OAuth2UserInfoDTO {

    private String id;
    private String name;
    private Provider provider;

    public abstract Member toEntity();
}
