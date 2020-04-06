package com.sproutt.eussyaeussyaapi.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sproutt.eussyaeussyaapi.api.member.dto.JwtMemberDTO;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Slf4j
@Component
public class JwtHelper {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final long VALID_MILLISECOND = 1000L * 60 * 60; // 1 hour

    private static final String CLAIM_KEY = "member";

    public String createToken(JwtMemberDTO jwtMemberDTO) {

        log.info("token: {}", secretKey);

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

    public JwtMemberDTO decryptToken(String token) {
        Jws<Claims> claims = null;

        try {

            claims = getClaims(token);

        } catch (Exception e) {

            throw new UnsupportedJwtException("token parser fail");
        }

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.convertValue(claims.getBody().get(CLAIM_KEY), JwtMemberDTO.class);
    }

    public boolean isUsable(String token) {
        try {
            Jws<Claims> claims = getClaims(token);

            return !isExpired(claims);

        } catch (Exception e) {
            return false;
        }
    }

    private boolean isExpired(Jws<Claims> claims) {
        return claims.getBody().getExpiration().before(new Date());
    }

    private Jws<Claims> getClaims(String token) {
        Jws<Claims> claims = Jwts.parser()
                                 .setSigningKey(this.generateKey())
                                 .parseClaimsJws(token);

        return claims;
    }

    private byte[] generateKey() {

        byte[] key = null;

        try {
            key = secretKey.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return key;
    }
}
