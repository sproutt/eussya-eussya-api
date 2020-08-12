package com.sproutt.eussyaeussyaapi.application.member;

import com.sproutt.eussyaeussyaapi.api.member.EmailAuthDTO;
import com.sproutt.eussyaeussyaapi.api.member.dto.JoinDTO;
import com.sproutt.eussyaeussyaapi.api.member.dto.JwtMemberDTO;
import com.sproutt.eussyaeussyaapi.api.member.dto.LoginDTO;
import com.sproutt.eussyaeussyaapi.application.MailService;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import com.sproutt.eussyaeussyaapi.domain.member.Provider;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.DuplicationMemberException;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.NoSuchMemberException;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.VerificationException;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.WrongPasswordException;
import com.sproutt.eussyaeussyaapi.utils.RandomGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Member login(LoginDTO loginDTO) {
        Member member = memberRepository.findByMemberId(loginDTO.getMemberId()).orElseThrow(NoSuchMemberException::new);

        if (!passwordEncoder.matches(loginDTO.getPassword(), member.getPassword())) {
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

        String authCode = RandomGenerator.createAuthenticationCode();
        mailService.sendAuthEmail(joinDTO.getMemberId(), authCode);

        Member member = Member.builder()
                              .memberId(joinDTO.getMemberId())
                              .password(passwordEncoder.encode(joinDTO.getPassword()))
                              .email(joinDTO.getMemberId())
                              .nickName(joinDTO.getNickName())
                              .authentication(authCode)
                              .provider(Provider.LOCAL)
                              .build();

        return memberRepository.save(member);
    }

    @Override
    @Transactional
    public Member sendAuthCodeToEmail(String email) {
        Member member = memberRepository.findByMemberId(email).orElseThrow(NoSuchMemberException::new);
        String authCode = RandomGenerator.createAuthenticationCode();

        member.changeAuthCode(authCode);
        mailService.sendAuthEmail(email, authCode);

        return memberRepository.save(member);
    }

    @Override
    public Member findTokenOwner(JwtMemberDTO jwtMemberDTO) {
        return memberRepository.findById(jwtMemberDTO.getId()).orElseThrow(NoSuchMemberException::new);
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
    public Member authenticateEmail(EmailAuthDTO emailAuthDTO) {
        Member member = memberRepository.findByMemberId(emailAuthDTO.getMemberId()).orElseThrow(NoSuchMemberException::new);

        if (!member.verifyEmail(emailAuthDTO.getAuthCode())) {
            throw new VerificationException();
        }

        return memberRepository.save(member);
    }
}
