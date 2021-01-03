package com.sproutt.eussyaeussyaapi.api.config.auth;

import com.sproutt.eussyaeussyaapi.api.member.dto.MemberTokenCommand;
import com.sproutt.eussyaeussyaapi.api.config.auth.exception.UnSupportedOAuth2Exception;
import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.NoSuchMemberException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    @Value("${jwt.secret}")
    private String secretKey;

    private final JwtHelper jwtHelper;
    private final MemberRepository memberRepository;

    public CustomOAuth2SuccessHandler(JwtHelper jwtHelper, MemberRepository memberRepository) {
        this.jwtHelper = jwtHelper;
        this.memberRepository = memberRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication) throws IOException {
        String prefix = getPrefix(authentication);
        String memberId = prefix + authentication.getName();
        MemberTokenCommand memberTokenCommand = memberRepository.findByMemberId(memberId).orElseThrow(NoSuchMemberException::new).toJwtInfo();

        String token = jwtHelper.createToken(secretKey, memberTokenCommand);
        res.getWriter().write(token);
    }

    private String getPrefix(Authentication authentication) {
        if (authentication.toString().contains("github")) {
            return "github_";
        }

        throw new UnSupportedOAuth2Exception();
    }
}