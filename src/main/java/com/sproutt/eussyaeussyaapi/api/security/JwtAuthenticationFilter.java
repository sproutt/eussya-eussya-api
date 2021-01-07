package com.sproutt.eussyaeussyaapi.api.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtHelper jwtHelper;

    public JwtAuthenticationFilter(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = ((HttpServletRequest) request).getHeader(JwtHelper.ACCESS_TOKEN_HEADER);

        if (token != null && jwtHelper.isUsable(token)) {
            Authentication authentication = jwtHelper.getAuthentication(token);
            SecurityContextHolder.getContext()
                                 .setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }
}

