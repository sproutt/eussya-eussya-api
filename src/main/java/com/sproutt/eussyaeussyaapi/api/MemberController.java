package com.sproutt.eussyaeussyaapi.api;

import com.sproutt.eussyaeussyaapi.application.MemberService;
import com.sproutt.eussyaeussyaapi.dto.OAuthMemberInfo;
import com.sproutt.eussyaeussyaapi.dto.OAuthResponseDTO;
import com.sproutt.eussyaeussyaapi.dto.SignUpDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/oauth")
    public ResponseEntity<OAuthMemberInfo> getOAuthMemberInfo(@RequestBody OAuthResponseDTO oauthResponseDTO) {
        OAuthMemberInfo oAuthMemberInfo = new OAuthMemberInfo(oauthResponseDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(oAuthMemberInfo, headers, HttpStatus.OK);
    }

    @GetMapping("/member/sign-up")
    public ResponseEntity createMember(@RequestBody SignUpDTO signUpDTO) {
        memberService.create(signUpDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity(headers, HttpStatus.OK);
    }

    @PostMapping("/member/sign-in")
    public ResponseEntity loginMember(HttpSession session, @RequestBody String accessToken) {
        memberService.login(session, accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}
