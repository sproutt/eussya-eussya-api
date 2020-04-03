package com.sproutt.eussyaeussyaapi.api.oauth2.service;

import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.NoSuchMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SocialService {

    private final MemberRepository memberRepository;

    public Member login(Member member) {

        if (!memberRepository.existsByMemberId(member.getMemberId())) {
            return memberRepository.save(member);
        }

        return memberRepository.findByMemberId(member.getMemberId()).orElseThrow(NoSuchMemberException::new);
    }
}
