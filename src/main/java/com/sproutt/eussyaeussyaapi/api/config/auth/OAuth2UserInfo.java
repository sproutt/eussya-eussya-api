package com.sproutt.eussyaeussyaapi.api.config.auth;

import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.Provider;

public interface OAuth2UserInfo {
    String getMemberId();
    String getNickName();
    String getEmail();
    Provider getProvider();
    Member toEntity();
}
