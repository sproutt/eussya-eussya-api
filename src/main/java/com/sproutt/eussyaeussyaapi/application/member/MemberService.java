package com.sproutt.eussyaeussyaapi.application.member;

import com.sproutt.eussyaeussyaapi.api.member.EmailAuthDTO;
import com.sproutt.eussyaeussyaapi.api.member.dto.JoinDTO;
import com.sproutt.eussyaeussyaapi.api.member.dto.JwtMemberDTO;
import com.sproutt.eussyaeussyaapi.api.member.dto.LoginDTO;
import com.sproutt.eussyaeussyaapi.api.oauth2.dto.OAuth2UserInfoDTO;
import com.sproutt.eussyaeussyaapi.domain.member.Member;

import javax.mail.MessagingException;
import java.util.List;

public interface MemberService {

    Member login(LoginDTO loginDTO);

    Member joinWithLocalProvider(JoinDTO joinDTO) throws MessagingException;

    Member authenticateEmail(EmailAuthDTO emailAuthDTO);

    Member sendAuthCodeToEmail(String email);

    Member findTokenOwner(JwtMemberDTO jwtMemberDTO);

    boolean isDuplicatedMemberId(String memberId);

    boolean isDuplicatedNickName(String nickName);

    Member findByMemberId(String memberId);

    List<Member> findAllExclude(String memberId);

    Member loginWithSocialProvider(OAuth2UserInfoDTO userInfoDTO);
}
