package com.sproutt.eussyaeussyaapi.api.member;

import com.sproutt.eussyaeussyaapi.api.member.dto.JoinDTO;
import com.sproutt.eussyaeussyaapi.api.member.dto.LoginDTO;
import com.sproutt.eussyaeussyaapi.api.member.dto.NickNameUpdateDTO;
import com.sproutt.eussyaeussyaapi.api.member.dto.PasswordUpdateDTO;
import com.sproutt.eussyaeussyaapi.api.security.JwtService;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
public class MemberController {

    @Value("${token.key}")
    private String TOKEN_KEY;

    private final MemberService memberService;
    private final JwtService jwtService;

    public MemberController(MemberService memberService, JwtService jwtService) {
        this.memberService = memberService;
        this.jwtService = jwtService;
    }

    @PostMapping("/members")
    public ResponseEntity createMemberWithLocalProvider(@Valid @RequestBody JoinDTO joinDTO) {

        memberService.joinWithLocalProvider(joinDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity loginMember(@Valid @RequestBody LoginDTO loginDTO) {
        Member loginMember = memberService.login(loginDTO);

        String token = jwtService.createToken(loginMember);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(TOKEN_KEY, token);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PostMapping("/email-auth")
    public ResponseEntity<String> authenticateEmail(@Valid @RequestBody EmailAuthDTO emailAuthDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        memberService.authenticateEmail(emailAuthDTO);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PutMapping("/members/{memberId}/nickname")
    public ResponseEntity updateMemberNickName(@Valid @RequestBody NickNameUpdateDTO nickNameUpdateDTO) {

        Member member = memberService.findMemberById(nickNameUpdateDTO.getMemberId());
        memberService.updateNickName(member, nickNameUpdateDTO.getNickName());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PutMapping("/members/{memberId}/password")
    public ResponseEntity updateMemberPassword(@Valid @RequestBody PasswordUpdateDTO passwordUpdateDTO) {

        Member member = memberService.findMemberById(passwordUpdateDTO.getMemberId());
        memberService.updatePassword(member, passwordUpdateDTO.getPassword());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}
