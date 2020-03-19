package com.sproutt.eussyaeussyaapi.application;

import com.sproutt.eussyaeussyaapi.api.dto.JoinDTO;
import com.sproutt.eussyaeussyaapi.api.dto.LoginDTO;
import com.sproutt.eussyaeussyaapi.domain.Member;
import com.sproutt.eussyaeussyaapi.domain.MemberRepository;
import com.sproutt.eussyaeussyaapi.domain.exceptions.DuplicationMemberException;
import com.sproutt.eussyaeussyaapi.domain.exceptions.NoSuchMemberException;
import com.sproutt.eussyaeussyaapi.domain.exceptions.WrongPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member login(LoginDTO loginDTO) {

        Member member = memberRepository.findByMemberId(loginDTO.getMemberId()).orElseThrow(NoSuchMemberException::new);

        if (!member.isEqualPassword(loginDTO.getPassword())) {
            throw new WrongPasswordException();
        }

        return member;
    }

    @Override
    public Member join(JoinDTO joinDTO) {

        if (memberRepository.findByMemberId(joinDTO.getMemberId()).orElse(null) != null) {
            throw new DuplicationMemberException();
        }

        Member member = Member.builder()
                              .memberId(joinDTO.getMemberId())
                              .password(joinDTO.getPassword())
                              .nickName(joinDTO.getNickName())
                              .build();

        return memberRepository.save(member);
    }
}
