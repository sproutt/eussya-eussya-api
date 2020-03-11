package com.sproutt.eussyaeussyaapi.config;

import com.sproutt.eussyaeussyaapi.application.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final static String SECRET_KEY = "secret";

    @Autowired
    private JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = request.getHeader(SECRET_KEY);

        if (token == null || !jwtService.isUsable(token)) {
            throw new RuntimeException();
        }

        return true;
    }
}
