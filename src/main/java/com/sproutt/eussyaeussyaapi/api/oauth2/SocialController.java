package com.sproutt.eussyaeussyaapi.api.oauth2;

import com.sproutt.eussyaeussyaapi.api.oauth2.dto.OAuth2UserInfoDTO;
import com.sproutt.eussyaeussyaapi.api.oauth2.exception.UnSupportedOAuth2Exception;
import com.sproutt.eussyaeussyaapi.api.oauth2.service.OAuth2RequestService;
import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.Provider;
import org.springframework.beans.factory.annotation.Qualifier;
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
    private final OAuth2RequestService githubOAuth2RequestService;
    private final OAuth2RequestService googleOAuth2RequestService;
    private final OAuth2RequestService facebookOAuth2RequestService;

    @Value("${jwt.header}")
    private String tokenKey;

    @Value("${jwt.secret}")
    private String secretKey;

    public SocialController(JwtHelper jwtHelper, MemberService memberService, @Qualifier("github") OAuth2RequestService githubOAuth2RequestService, @Qualifier("google") OAuth2RequestService googleOAuth2RequestService, @Qualifier("facebook") OAuth2RequestService facebookOAuth2RequestService) {
        this.jwtHelper = jwtHelper;
        this.memberService = memberService;
        this.githubOAuth2RequestService = githubOAuth2RequestService;
        this.googleOAuth2RequestService = googleOAuth2RequestService;
        this.facebookOAuth2RequestService = facebookOAuth2RequestService;
    }

    @PostMapping("/login/{provider}")
    public ResponseEntity loginByProvider(@PathVariable String provider, @RequestParam String accessToken) {
        OAuth2RequestService oAuth2RequestService = getProviderRequestService(provider);

        OAuth2UserInfoDTO userInfoDTO = oAuth2RequestService.getUserInfo(accessToken);
        Member loginMember = userInfoDTO.toEntity();

        memberService.loginWithSocialProvider(userInfoDTO);
        String token = jwtHelper.createToken(secretKey, loginMember.toJwtInfo());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(tokenKey, token);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    private OAuth2RequestService getProviderRequestService(String provider) {
        if (Provider.GITHUB.equals(provider)) {
            return githubOAuth2RequestService;
        }

        if (Provider.GOOGLE.equals(provider)) {
            return googleOAuth2RequestService;
        }

        if (Provider.FACEBOOK.equals(provider)) {
            return facebookOAuth2RequestService;
        }

        throw new UnSupportedOAuth2Exception();
    }

}
