package com.sproutt.eussyaeussyaapi.api.security;

import com.sproutt.eussyaeussyaapi.api.security.exception.InvalidAccessTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Value("${jwt.accessTokenKey}")
    private String accessTokenKey;

    @Value("${jwt.refreshTokenKey}")
    private String refreshTokenKey;

    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    private JwtHelper jwtHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            log.info("COORS is passed");
            return true;
        }

        String accessToken = request.getHeader(accessTokenKey);

        if (!jwtHelper.isUsable(secretKey, accessToken)) {
            throw new InvalidAccessTokenException();
        }

        return true;
    }
}
