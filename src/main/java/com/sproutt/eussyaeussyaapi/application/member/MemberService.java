package com.sproutt.eussyaeussyaapi.application.member;

import com.sproutt.eussyaeussyaapi.api.member.EmailAuthDTO;
import com.sproutt.eussyaeussyaapi.api.member.dto.JoinDTO;
import com.sproutt.eussyaeussyaapi.api.member.dto.LoginDTO;
import com.sproutt.eussyaeussyaapi.domain.member.Member;

public interface MemberService {

    Member login(LoginDTO loginDTO);

    Member joinWithLocalProvider(JoinDTO joinDTO);

    Member joinWithOAuth2Provider();

    Member authenticateEmail(EmailAuthDTO emailAuthDTO);

    Member sendAuthCodeToEmail(String email);

    boolean isDuplicatedMemberId(String memberId);
}
