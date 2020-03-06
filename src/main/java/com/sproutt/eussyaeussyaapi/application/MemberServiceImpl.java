package com.sproutt.eussyaeussyaapi.application;

import com.sproutt.eussyaeussyaapi.domain.Member;
import com.sproutt.eussyaeussyaapi.domain.MemberRepository;
import com.sproutt.eussyaeussyaapi.dto.SignUpDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    final MemberRepository memberRepository;

    @Override
    public Member findByMemberId(String memberId) {
        return memberRepository.findByMemberId(memberId).orElseThrow(RuntimeException::new);
    }

    @Override
    public Member findByName(String name) {
        return memberRepository.findByName(name).orElseThrow(RuntimeException::new);
    }

    @Override
    public Member findByNickName(String nickName) {
        return memberRepository.findByNickName(nickName).orElseThrow(RuntimeException::new);
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
    }

    @Override
    public Member updateNickName(String memberId, String newNickName) {
        Member member = findByMemberId(memberId);
        member.updateNickName(newNickName);

        return memberRepository.save(member);
    }

    @Override
    public void login(HttpSession session, String accessToken) {
    }

    @Override
    public Member create(SignUpDTO signUpDTO) {
        Member newMember = new Member(signUpDTO);

        return memberRepository.save(newMember);
    }
}
