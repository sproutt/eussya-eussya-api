package com.sproutt.eussyaeussyaapi.api.security;

import com.sproutt.eussyaeussyaapi.domain.member.Member;

public interface JwtService {

    String createToken(Member member);

    Member decryptToken(String token);

    boolean isUsable(String token);
}
