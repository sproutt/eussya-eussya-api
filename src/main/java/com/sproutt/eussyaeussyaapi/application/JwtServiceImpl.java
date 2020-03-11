package com.sproutt.eussyaeussyaapi.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sproutt.eussyaeussyaapi.domain.Member;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    private static final String CLAIM_KEY = "member";
    private static final String SECRET_KEY = "secret";

    @Override
    public String createToken(Member member) {

        String token = Jwts.builder()
                .setHeaderParam("typ", "jwt")
                .claim(CLAIM_KEY, member)
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();

        return token;
    }

    @Override
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

    @Override
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
