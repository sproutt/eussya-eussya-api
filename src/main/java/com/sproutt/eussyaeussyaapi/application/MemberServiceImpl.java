package com.sproutt.eussyaeussyaapi.application;

import com.sproutt.eussyaeussyaapi.api.dto.JoinDTO;
import com.sproutt.eussyaeussyaapi.api.dto.LoginDTO;
import com.sproutt.eussyaeussyaapi.domain.Member;
import com.sproutt.eussyaeussyaapi.domain.MemberRepository;
import com.sproutt.eussyaeussyaapi.domain.exceptions.DuplicatedMemberIdException;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public Member login(LoginDTO loginDTO) {

        Member member = memberRepository.findByMemberId(loginDTO.getMemberId()).orElseThrow(RuntimeException::new);

        if (!member.isEqualPassword(loginDTO.getPassword())) {
            throw new RuntimeException();
        }

        return member;
    }

    @Override
    public Member join(JoinDTO joinDTO) {

        if (memberRepository.findByMemberId(joinDTO.getMemberId()).orElse(null) != null) {
            throw new DuplicatedMemberIdException();
        }

        Member member = Member.builder()
                .memberId(joinDTO.getMemberId())
                .password(joinDTO.getPassword())
                .name(joinDTO.getNickName())
                .build();

        return memberRepository.save(member);
    }
}
