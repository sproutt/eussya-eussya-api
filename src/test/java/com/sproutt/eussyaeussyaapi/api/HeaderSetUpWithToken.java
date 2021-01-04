package com.sproutt.eussyaeussyaapi.api;

import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.api.security.JwtAuthenticationFilter;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZonedDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class HeaderSetUpWithToken {

    @Value("${jwt.header}")
    private String tokenKey;

    @Value("${jwt.secret}")
    private String secretKey;
    @MockBean
    private JwtHelper jwtHelper;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    public HttpHeaders setUpHeader() throws Exception {
        Member loginMember = MemberFactory.getDefaultMember();
        when(jwtHelper.createToken(any(), any())).thenReturn("token");
        String token = jwtHelper.createToken(secretKey, loginMember.toJwtInfo());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(tokenKey, token);
        headers.setZonedDateTime("date", ZonedDateTime.now());

//        when(jwtAuthenticationFilter.doFilter(any(), any(), any())).thenReturn(true);
        when(jwtHelper.decryptToken(secretKey, token)).thenReturn(loginMember.toJwtInfo());

        return headers;
    }
}
