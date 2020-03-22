package com.sproutt.eussyaeussyaapi.api.oauth2;

import com.sproutt.eussyaeussyaapi.api.oauth2.dto.GithubOAuth2UserDto;
import com.sproutt.eussyaeussyaapi.api.oauth2.service.GithubOAuth2Service;
import com.sproutt.eussyaeussyaapi.api.oauth2.service.GoogleOAuth2Service;
import com.sproutt.eussyaeussyaapi.api.security.JwtService;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.DuplicationMemberException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/social")
@RestController
public class SocialController {

    private final GithubOAuth2Service githubOAuth2Service;
    private final GoogleOAuth2Service googleOAuth2Service;
    private final JwtService jwtService;

    @Value("${token.key}")
    private String TOKEN_KEY;

    @PostMapping("/signin/{provider}")
    public ResponseEntity signInByProvider(@PathVariable String provider, @RequestParam String accessToken) {

        Member member = githubOAuth2Service.getMemberInfo(accessToken);

        String token = jwtService.createToken(member);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(TOKEN_KEY, token);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PostMapping("/signup/{provider}")
    public ResponseEntity signUpByProvider(@PathVariable String provider, @RequestParam String accessToken) {

        githubOAuth2Service.createMember(accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

}
