package com.sproutt.eussyaeussyaapi.application;

import com.sproutt.eussyaeussyaapi.domain.Member;
import com.sproutt.eussyaeussyaapi.dto.SignUpDTO;

import javax.servlet.http.HttpSession;

public interface MemberService {

    Member findByMemberId(String MemberId);

    Member findByName(String name);

    Member findByNickName(String nickName);

    Member findByEmail(String email);

    Member updateNickName(String memberId, String newNickName);

    void login(HttpSession session, String accessToken);

    Member create(SignUpDTO signUpDTO);
}
