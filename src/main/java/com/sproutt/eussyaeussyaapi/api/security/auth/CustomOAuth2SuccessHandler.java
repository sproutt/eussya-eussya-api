package com.sproutt.eussyaeussyaapi.api.security.auth;

import com.sproutt.eussyaeussyaapi.api.member.dto.MemberTokenCommand;
import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.NoSuchMemberException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomOAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${oauth2.authorizedRedirectUrl}")
    private String authorizedRedirectUrl;

    private final JwtHelper jwtHelper;
    private final MemberRepository memberRepository;

    public CustomOAuth2SuccessHandler(JwtHelper jwtHelper, MemberRepository memberRepository) {
        this.jwtHelper = jwtHelper;
        this.memberRepository = memberRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest req, HttpServletResponse res, Authentication authentication) throws IOException {
        String memberId = authentication.getName();
        MemberTokenCommand memberTokenCommand = memberRepository.findByMemberId(memberId).orElseThrow(NoSuchMemberException::new).toJwtInfo();

        String accessToken = jwtHelper.createAccessToken(memberTokenCommand);
        String refreshToken = jwtHelper.createRefreshToken(memberTokenCommand);

        String query = "?accessToken=" + accessToken + "&refreshToken=" + refreshToken;

        getRedirectStrategy().sendRedirect(req, res, authorizedRedirectUrl + query);
        super.clearAuthenticationAttributes(req);
    }
}