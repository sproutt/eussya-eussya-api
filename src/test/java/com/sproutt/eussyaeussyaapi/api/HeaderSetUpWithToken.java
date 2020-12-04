package com.sproutt.eussyaeussyaapi.api;

import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.api.security.JwtInterceptor;
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

    @Value("${jwt.accessTokenKey}")
    private String accessTokenKey;

    @Value("${jwt.secret}")
    private String secretKey;
    @MockBean
    private JwtHelper jwtHelper;

    @MockBean
    private JwtInterceptor jwtInterceptor;


    public HttpHeaders setUpHeader() throws Exception {
        Member loginMember = MemberFactory.getDefaultMember();
        when(jwtHelper.createAccessToken(any(), any())).thenReturn("token");
        String token = jwtHelper.createAccessToken(secretKey, loginMember.toJwtInfo());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(accessTokenKey, token);
        headers.setZonedDateTime("date", ZonedDateTime.now());

        when(jwtInterceptor.preHandle(any(), any(), any())).thenReturn(true);
        when(jwtHelper.decryptToken(secretKey, token)).thenReturn(loginMember.toJwtInfo());

        return headers;
    }
}
