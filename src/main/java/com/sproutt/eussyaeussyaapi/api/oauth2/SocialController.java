package com.sproutt.eussyaeussyaapi.api.oauth2;

import com.sproutt.eussyaeussyaapi.api.oauth2.dto.OAuth2UserInfoDTO;
import com.sproutt.eussyaeussyaapi.api.oauth2.service.OAuth2RequestService;
import com.sproutt.eussyaeussyaapi.api.oauth2.service.OAuth2RequestServiceFactory;
import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/social")
@RestController
public class SocialController {

    private final JwtHelper jwtHelper;
    private final MemberService memberService;
    private final OAuth2RequestServiceFactory oAuth2RequestServiceFactory;

    @Value("${jwt.accessTokenKey}")
    private String accessTokenKey;

    @Value("${jwt.secret}")
    private String secretKey;

    public SocialController(JwtHelper jwtHelper, MemberService memberService, OAuth2RequestServiceFactory oAuth2RequestServiceFactory) {
        this.jwtHelper = jwtHelper;
        this.memberService = memberService;
        this.oAuth2RequestServiceFactory = oAuth2RequestServiceFactory;
    }

    @PostMapping("/login/{provider}")
    public ResponseEntity loginByProvider(@PathVariable String provider, @RequestParam String accessToken) {
        OAuth2RequestService oAuth2RequestService = oAuth2RequestServiceFactory.getOAuth2RequestService(provider);

        OAuth2UserInfoDTO userInfoDTO = oAuth2RequestService.getUserInfo(accessToken);
        Member loginMember = userInfoDTO.toEntity();

        memberService.loginWithSocialProvider(userInfoDTO);
        String token = jwtHelper.createAccessToken(secretKey, loginMember.toJwtInfo());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(accessTokenKey, token);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }
}
