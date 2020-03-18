package com.sproutt.eussyaeussyaapi.application.member;

import com.sproutt.eussyaeussyaapi.api.member.dto.JoinDTO;
import com.sproutt.eussyaeussyaapi.api.member.dto.LoginDTO;
import com.sproutt.eussyaeussyaapi.domain.member.Member;

public interface MemberService {

    Member login(LoginDTO loginDTO);

    Member join(JoinDTO joinDTO);
}
