package com.sproutt.eussyaeussyaapi.application;

import com.sproutt.eussyaeussyaapi.domain.Member;

public interface JwtService {

    String createToken(Member member);

    Member decryptToken(String token);

    boolean isUsable(String token);
}
