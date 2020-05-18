package com.sproutt.eussyaeussyaapi.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sproutt.eussyaeussyaapi.api.member.dto.JwtMemberDTO;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtHelper {

    private static final long VALID_MILLISECOND = 1000L * 60 * 60; // 1 hour

    private static final String CLAIM_KEY = "member";

    public String createToken(String secretKey, JwtMemberDTO jwtMemberDTO) {

        Claims claims = Jwts.claims().setSubject(CLAIM_KEY);
        claims.put(CLAIM_KEY, jwtMemberDTO);

        Date now = new Date();

        String token = Jwts.builder()
                           .setHeaderParam("typ", "jwt")
                           .setClaims(claims)
                           .setIssuedAt(now)
                           .setExpiration(new Date(now.getTime() + VALID_MILLISECOND))
                           .signWith(SignatureAlgorithm.HS256, secretKey)
                           .compact();

        return token;
    }

    public JwtMemberDTO decryptToken(String secretKey, String token) {
        Jws<Claims> claims = null;

        try {

            claims = getClaims(secretKey, token);

        } catch (Exception e) {

            throw new UnsupportedJwtException("token parser fail");
        }

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.convertValue(claims.getBody().get(CLAIM_KEY), JwtMemberDTO.class);
    }

    public boolean isUsable(String secretKey, String token) {
        try {
            Jws<Claims> claims = getClaims(secretKey, token);

            return !isExpired(claims);

        } catch (Exception e) {
            return false;
        }
    }

    private boolean isExpired(Jws<Claims> claims) {
        return claims.getBody().getExpiration().before(new Date());
    }

    private Jws<Claims> getClaims(String secretKey, String token) {
        Jws<Claims> claims = Jwts.parser()
                                 .setSigningKey(secretKey)
                                 .parseClaimsJws(token);

        return claims;
    }
}
