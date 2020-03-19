package com.sproutt.eussyaeussyaapi.api.member;

import com.sproutt.eussyaeussyaapi.api.dto.EmailDTO;
import com.sproutt.eussyaeussyaapi.api.member.dto.JoinDTO;
import com.sproutt.eussyaeussyaapi.api.member.dto.LoginDTO;
import com.sproutt.eussyaeussyaapi.api.security.JwtService;
import com.sproutt.eussyaeussyaapi.application.MailService;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MemberController {

    @Value("${token.key}")
    private String TOKEN_KEY;

    private final MemberService memberService;
    private final JwtService jwtService;
    private final MailService mailService;

    public MemberController(MemberService memberService, JwtService jwtService, MailService mailService) {
        this.memberService = memberService;
        this.jwtService = jwtService;
        this.mailService = mailService;
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@Valid @RequestBody JoinDTO joinDTO) {

        memberService.join(joinDTO);

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
    public ResponseEntity<String> sendAuthEmail(@Valid @RequestBody EmailDTO emailDTO) {
        String authCode = mailService.sendAuthEmail(emailDTO.getEmail());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(authCode, headers, HttpStatus.OK);
    }
}
