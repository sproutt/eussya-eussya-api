package com.sproutt.eussyaeussyaapi.application.member;

import com.sproutt.eussyaeussyaapi.api.member.dto.JoinDTO;
import com.sproutt.eussyaeussyaapi.api.member.dto.LoginDTO;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.DuplicationMemberException;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.NoSuchMemberException;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.WrongPasswordException;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
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
