package com.sproutt.eussyaeussyaapi.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sproutt.eussyaeussyaapi.api.member.dto.MemberTokenCommand;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;

@Component
public class JwtHelper {

    @Value("${jwt.secret}")
    private String secretKey;

    private static final long ACCESS_TOKEN_VALID_MILLISECOND = 1000L * 60 * 60 * 24; // 24 hour
    private static final long REFRESH_TOKEN_VALID_MILLISECOND = 7 * 1000L * 60 * 60 * 24; // 7 Days

    private static final String CLAIM_KEY = "member";
    public static final String ACCESS_TOKEN_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "Refresh-Authorization";

    public String createAccessToken(MemberTokenCommand memberTokenCommand) {
        return createToken(memberTokenCommand, ACCESS_TOKEN_VALID_MILLISECOND);
    }

    public String createRefreshToken(MemberTokenCommand memberTokenCommand) {
        String token = createToken(memberTokenCommand, REFRESH_TOKEN_VALID_MILLISECOND);
        // TODO: 20. 12. 4. Redis 연동하기
        return token;
    }

    private String createToken(MemberTokenCommand memberTokenCommand, long validMilisecond) {
        Claims claims = Jwts.claims().setSubject(CLAIM_KEY);
        claims.put(CLAIM_KEY, memberTokenCommand);

        Date now = new Date();

        String token = Jwts.builder()
                           .setHeaderParam("typ", "jwt")
                           .setClaims(claims)
                           .setIssuedAt(now)
                           .setExpiration(new Date(now.getTime() + validMilisecond))
                           .signWith(SignatureAlgorithm.HS256, secretKey)
                           .compact();

        return token;
    }

    public MemberTokenCommand decryptToken(String token) {
        Jws<Claims> claims = getClaims(token);
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.convertValue(claims.getBody().get(CLAIM_KEY), MemberTokenCommand.class);
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

        try {
            Jws<Claims> claims = Jwts.parser()
                                     .setSigningKey(secretKey)
                                     .parseClaimsJws(token);
            return claims;
        } catch (Exception e) {
            throw new UnsupportedJwtException("token parser fail");
        }
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token) {
        MemberTokenCommand memberTokenCommand = decryptToken(token);
        return new UsernamePasswordAuthenticationToken(memberTokenCommand, "", Collections.singletonList(new SimpleGrantedAuthority(memberTokenCommand.getRole().getKey())));
    }
}
