package com.sproutt.eussyaeussyaapi.api.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtHelper jwtHelper;

    public JwtAuthenticationFilter(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String token = request.getHeader(JwtHelper.ACCESS_TOKEN_HEADER);

        if (token != null && jwtHelper.isUsable(token)) {
            Authentication authentication = jwtHelper.getAuthentication(token);
            SecurityContextHolder.getContext()
                                 .setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}

