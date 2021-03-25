package com.sproutt.eussyaeussyaapi.application.member;

import com.sproutt.eussyaeussyaapi.api.member.EmailAuthCommand;
import com.sproutt.eussyaeussyaapi.api.member.dto.MemberJoinCommand;
import com.sproutt.eussyaeussyaapi.api.member.dto.MemberLoginCommand;
import com.sproutt.eussyaeussyaapi.api.member.dto.MemberTokenCommand;
import com.sproutt.eussyaeussyaapi.domain.member.Member;

import javax.mail.MessagingException;
import java.util.List;

public interface MemberService {

    Member login(MemberLoginCommand memberLoginCommand);

    Member joinWithLocalProvider(MemberJoinCommand memberJoinCommand) throws MessagingException;

    Member authenticateEmail(EmailAuthCommand emailAuthCommand);

    Member sendAuthCodeToEmail(String email);

    Member findTokenOwner(MemberTokenCommand memberTokenCommand);

    boolean isDuplicatedMemberId(String memberId);

    boolean isDuplicatedNickName(String nickName);

    Member findByMemberId(String memberId);

    List<Member> findAllExclude(String memberId);

    Member updateProfilePath(Member loginMember, String profilePath);
}
