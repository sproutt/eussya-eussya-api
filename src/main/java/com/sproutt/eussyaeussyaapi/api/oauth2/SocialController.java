package com.sproutt.eussyaeussyaapi.api.oauth2;

import com.sproutt.eussyaeussyaapi.api.oauth2.service.OAuth2Service;
import com.sproutt.eussyaeussyaapi.api.oauth2.service.OAuth2ServiceFactory;
import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/social")
@RestController
public class SocialController {

    private final JwtHelper jwtHelper;

    @Value("${token.key}")
    private String TOKEN_KEY;

    @PostMapping("/signin/{provider}")
    public ResponseEntity signInByProvider(@PathVariable String provider, @RequestParam String accessToken) {

        OAuth2Service oAuth2Service = OAuth2ServiceFactory.getOAuth2Service(provider);
        Member member = oAuth2Service.getMemberInfo(accessToken);

        String token = jwtHelper.createToken(member);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(TOKEN_KEY, token);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PostMapping("/signup/{provider}")
    public ResponseEntity signUpByProvider(@PathVariable String provider, @RequestParam String accessToken) {

        OAuth2Service oAuth2Service = OAuth2ServiceFactory.getOAuth2Service(provider);
        oAuth2Service.createMember(accessToken);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

}
