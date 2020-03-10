package com.sproutt.eussyaeussyaapi.application;

import com.sproutt.eussyaeussyaapi.domain.Member;
import com.sproutt.eussyaeussyaapi.domain.dto.JoinDTO;

import javax.servlet.http.HttpSession;

public interface MemberService {

    void login(HttpSession session, String accessToken);

    Member join(JoinDTO joinDTO);
}
