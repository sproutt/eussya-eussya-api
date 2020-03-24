package com.sproutt.eussyaeussyaapi.api.oauth2.service;

import com.sproutt.eussyaeussyaapi.domain.member.Member;

public interface OAuth2Service {

    Member getMemberInfo(String accessToken);

    Member createMember(String accessToken);

    String getProvider();

}
