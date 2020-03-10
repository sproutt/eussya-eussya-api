package com.sproutt.eussyaeussyaapi.application;

import com.sproutt.eussyaeussyaapi.api.exceptions.DuplicatedMemberIdException;
import com.sproutt.eussyaeussyaapi.domain.Member;
import com.sproutt.eussyaeussyaapi.domain.MemberRepository;
import com.sproutt.eussyaeussyaapi.domain.dto.JoinDTO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void login(HttpSession session, String accessToken) {
        // jwt로 적용 예정
    }

    @Override
    public Member join(JoinDTO joinDTO) {

        if (memberRepository.findByMemberId(joinDTO.getMemberId()).isPresent()) {
            throw new DuplicatedMemberIdException();
        }

        Member member = joinDTO.toEntity();

        return memberRepository.save(member);
    }
}
