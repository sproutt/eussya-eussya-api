package com.sproutt.eussyaeussyaapi.application.member;

import com.sproutt.eussyaeussyaapi.api.member.EmailAuthCommand;
import com.sproutt.eussyaeussyaapi.api.member.dto.MemberJoinCommand;
import com.sproutt.eussyaeussyaapi.api.member.dto.MemberLoginCommand;
import com.sproutt.eussyaeussyaapi.api.member.dto.MemberTokenCommand;
import com.sproutt.eussyaeussyaapi.application.MailService;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import com.sproutt.eussyaeussyaapi.domain.member.Provider;
import com.sproutt.eussyaeussyaapi.domain.member.Role;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.DuplicationMemberException;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.NoSuchMemberException;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.VerificationException;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.WrongPasswordException;
import com.sproutt.eussyaeussyaapi.utils.RandomGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member login(MemberLoginCommand memberLoginCommand) {
        Member member = memberRepository.findByMemberId(memberLoginCommand.getMemberId()).orElseThrow(NoSuchMemberException::new);

        if (!passwordEncoder.matches(memberLoginCommand.getPassword(), member.getPassword())) {
            throw new WrongPasswordException();
        }

        return member;
    }

    @Override
    @Transactional
    public Member joinWithLocalProvider(MemberJoinCommand memberJoinCommand) {
        if (memberRepository.findByMemberId(memberJoinCommand.getMemberId()).orElse(null) != null) {
            throw new DuplicationMemberException();
        }

        Member member = Member.builder()
                .memberId(memberJoinCommand.getMemberId())
                .password(passwordEncoder.encode(memberJoinCommand.getPassword()))
                .email(memberJoinCommand.getMemberId())
                .nickName(memberJoinCommand.getNickName())
                .authentication("N")
                .role(Role.USER)
                .provider(Provider.LOCAL)
                .build();

        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public Member sendAuthCodeToEmail(String email) {
        Member member = memberRepository.findByMemberId(email).orElseThrow(NoSuchMemberException::new);
        String authCode = RandomGenerator.createAuthenticationCode();

        member.changeAuthCode(passwordEncoder.encode(authCode));
        mailService.sendAuthEmail(email, authCode);

        return memberRepository.save(member);
    }

    @Override
    public Member findTokenOwner(MemberTokenCommand memberTokenCommand) {
        return memberRepository.findById(memberTokenCommand.getId()).orElseThrow(NoSuchMemberException::new);
    }

    @Override
    public boolean isDuplicatedMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId).isPresent();
    }

    @Override
    public boolean isDuplicatedNickName(String nickName) {
        return memberRepository.findByNickName(nickName).isPresent();
    }

    @Override
    public Member findByMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId).orElseThrow(NoSuchMemberException::new);
    }

    @Override
    public List<Member> findAllExclude(String memberId) {
        if (memberId == null) {
            return memberRepository.findAll();
        }

        return memberRepository.findAll().stream().filter(member -> !member.getMemberId().equals(memberId)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Member authenticateEmail(EmailAuthCommand emailAuthCommand) {
        Member member = memberRepository.findByMemberId(emailAuthCommand.getMemberId()).orElseThrow(NoSuchMemberException::new);

        if (!passwordEncoder.matches(emailAuthCommand.getAuthCode(), member.getAuthentication())) {
            throw new VerificationException();
        }
        member.verifyEmail();

        return memberRepository.save(member);
    }

    @Override
    public Member updateProfilePath(Member loginMember, String profilePath) {
        loginMember.saveProfilePath(profilePath);
        return memberRepository.save(loginMember);
    }

    @Override
    public boolean isSameUser(Member member, Long id) {
        return member.getId().equals(id);
    }

    @Override
    public String getProfilePathById(Long id) {
        return memberRepository.findById(id).orElseThrow(RuntimeException::new).getProfilePath();
    }
}
