package com.sproutt.eussyaeussyaapi.api.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Component
public class JwtHelper {

    private static final String CLAIM_KEY = "member";
    private static final String SECRET_KEY = "secret";

    public String createToken(Member member) {

        String token = Jwts.builder()
                           .setHeaderParam("typ", "jwt")
                           .claim(CLAIM_KEY, member)
                           .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                           .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                           .compact();

        return token;
    }

    public Member decryptToken(String token) {
        Jws<Claims> claims = null;

        try {

            claims = getClaims(token);

        } catch (Exception e) {

            throw new UnsupportedJwtException("token parser fail");
        }

        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.convertValue(claims.getBody().get(CLAIM_KEY), Member.class);
    }

    public boolean isUsable(String token) {
        try{

            Jws<Claims> claims = getClaims(token);

            return true;

        }catch (Exception e) {
            throw new RuntimeException();
        }
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
            key = SECRET_KEY.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return key;
    }
}
