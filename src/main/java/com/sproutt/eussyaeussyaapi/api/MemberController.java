package com.sproutt.eussyaeussyaapi.api;

import com.sproutt.eussyaeussyaapi.application.MemberService;
import com.sproutt.eussyaeussyaapi.api.dto.JoinDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/members")
    public ResponseEntity createMember(@RequestBody JoinDTO joinDTO) {
        memberService.join(joinDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity loginMember(HttpSession session, @RequestBody String accessToken) {
        memberService.login(session, accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}
