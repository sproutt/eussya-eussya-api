package com.sproutt.eussyaeussyaapi.api.security.auth;

import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.Provider;

import java.util.Map;

public interface OAuth2UserInfo {
    String getMemberId();
    String getNickName();
    String getEmail();
    Provider getProvider();
    Map<String, Object> getAttributes();
    Member toEntity();
}
