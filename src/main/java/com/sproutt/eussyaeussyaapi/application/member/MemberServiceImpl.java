package com.sproutt.eussyaeussyaapi.application.member;

import com.sproutt.eussyaeussyaapi.api.member.EmailAuthDTO;
import com.sproutt.eussyaeussyaapi.api.member.dto.JoinDTO;
import com.sproutt.eussyaeussyaapi.api.member.dto.LoginDTO;
import com.sproutt.eussyaeussyaapi.api.member.dto.NickNameUpdateDTO;
import com.sproutt.eussyaeussyaapi.application.MailService;
import com.sproutt.eussyaeussyaapi.domain.member.Provider;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.*;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MailService mailService;

    @Override
    public Member login(LoginDTO loginDTO) {

        Member member = memberRepository.findByMemberId(loginDTO.getMemberId()).orElseThrow(NoSuchMemberException::new);

        if (!member.isEqualPassword(loginDTO.getPassword())) {
            throw new WrongPasswordException();
        }

        return member;
    }

    @Override
    @Transactional
    public Member joinWithLocalProvider(JoinDTO joinDTO) {

        if (memberRepository.findByMemberId(joinDTO.getMemberId()).orElse(null) != null) {
            throw new DuplicationMemberException();
        }

        String authCode = mailService.sendAuthEmail(joinDTO.getMemberId());

        Member member = Member.builder()
                              .memberId(joinDTO.getMemberId())
                              .password(joinDTO.getPassword())
                              .email(joinDTO.getMemberId())
                              .nickName(joinDTO.getNickName())
                              .authentication(authCode)
                              .provider(Provider.LOCAL)
                              .build();

        return memberRepository.save(member);
    }

    @Override
    public Member joinWithOAuth2Provider() {
        return null;
    }

    @Override
    @Transactional
    public Member authenticateEmail(EmailAuthDTO emailAuthDTO) {

        Member member = memberRepository.findByMemberId(emailAuthDTO.getMemberId()).orElseThrow(NoSuchMemberException::new);

        if (!member.verifyEmail(emailAuthDTO.getAuthCode())) {
            throw new VerificationException();
        }

        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public Member updateNickName(Member member, String nickName) {

        if (memberRepository.existsByNickName(nickName)) {
            throw new DuplicationNickNameException();
        }

        member.updateNickName(nickName);

        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public Member updatePassword(Member member, String password) {

        member.updatePassword(password);

        return memberRepository.save(member);
    }


    @Override
    @Transactional
    public Member findMemberById(String memberId) {

        return memberRepository.findByMemberId(memberId).orElseThrow(NoSuchMemberException::new);
    }
}
