package com.sproutt.eussyaeussyaapi.application;

import com.sproutt.eussyaeussyaapi.api.dto.JoinDTO;
import com.sproutt.eussyaeussyaapi.api.dto.LoginDTO;
import com.sproutt.eussyaeussyaapi.domain.Member;

public interface MemberService {

    Member login(LoginDTO loginDTO);

    Member join(JoinDTO joinDTO);
}
