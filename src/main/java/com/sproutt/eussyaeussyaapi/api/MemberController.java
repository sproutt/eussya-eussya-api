package com.sproutt.eussyaeussyaapi.api;

import com.sproutt.eussyaeussyaapi.api.dto.EmailDTO;
import com.sproutt.eussyaeussyaapi.api.dto.JoinDTO;
import com.sproutt.eussyaeussyaapi.api.dto.LoginDTO;
import com.sproutt.eussyaeussyaapi.application.JwtService;
import com.sproutt.eussyaeussyaapi.application.MailService;
import com.sproutt.eussyaeussyaapi.application.MemberService;
import com.sproutt.eussyaeussyaapi.domain.Member;
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
    private static String TOKEN_KEY;

    private final MemberService memberService;
    private final JwtService jwtService;
    private final MailService mailService;

    public MemberController(MemberService memberService, JwtService jwtService, MailService mailService) {
        this.memberService = memberService;
        this.jwtService = jwtService;
        this.mailService = mailService;
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody JoinDTO joinDTO) {
        System.out.println("token_key: " + TOKEN_KEY);

        memberService.join(joinDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity loginMember(@RequestBody LoginDTO loginDTO) {
        Member loginMember = memberService.login(loginDTO);

        String token = jwtService.createToken(loginMember);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(TOKEN_KEY, token);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PostMapping("/email-auth")
    public ResponseEntity<String> sendAuthEmail(@RequestBody @Valid EmailDTO emailDTO) {
        String authCode = mailService.sendAuthEmail(emailDTO.getEmail());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(authCode, headers, HttpStatus.OK);
    }
}
