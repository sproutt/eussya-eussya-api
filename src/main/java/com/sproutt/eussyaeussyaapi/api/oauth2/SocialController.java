package com.sproutt.eussyaeussyaapi.api.oauth2;

import com.sproutt.eussyaeussyaapi.api.oauth2.service.SocialService;
import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/social")
@RestController
public class SocialController {

    private final JwtHelper jwtHelper;
    private final SocialService socialService;

    @Value("${token.key}")
    private String TOKEN_KEY;

    @PostMapping("/login/{provider}")
    public ResponseEntity loginByProvider(@PathVariable String provider, @RequestParam String accessToken) throws Exception{
        Member member = socialService.login(accessToken, provider);
        String token = jwtHelper.createToken(member);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(TOKEN_KEY, token);
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

}
