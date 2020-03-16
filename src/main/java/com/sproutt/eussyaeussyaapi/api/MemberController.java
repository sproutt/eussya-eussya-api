package com.sproutt.eussyaeussyaapi.api;

import com.sproutt.eussyaeussyaapi.api.dto.JoinDTO;
import com.sproutt.eussyaeussyaapi.api.dto.LoginDTO;
import com.sproutt.eussyaeussyaapi.application.JwtService;
import com.sproutt.eussyaeussyaapi.application.MemberService;
import com.sproutt.eussyaeussyaapi.domain.Member;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {
    private static final String TOKEN_KEY = "Authorization";

    private final MemberService memberService;

    private final JwtService jwtService;

    public MemberController(MemberService memberService, JwtService jwtService) {
        this.memberService = memberService;
        this.jwtService = jwtService;
    }
    @GetMapping("/success")
    public String success(){
        return "success";
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody JoinDTO joinDTO) {
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
}
