package com.sproutt.eussyaeussyaapi.api.security;

import com.sproutt.eussyaeussyaapi.api.security.dto.JwtDTO;
import com.sproutt.eussyaeussyaapi.api.security.exception.InvalidRefreshTokenException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private JwtHelper jwtHelper;

    @PostMapping("/auth/accesstoken")
    public ResponseEntity<JwtDTO> issueNewAccessToken(@RequestHeader HttpHeaders requestHeaders) {
        String refreshToken = requestHeaders.getFirst(JwtHelper.REFRESH_TOKEN_HEADER);

        if (!jwtHelper.isUsable(refreshToken)) {
            throw new InvalidRefreshTokenException();
        }

        // TODO redis 정보랑 token payload 비교
        String accessToken = jwtHelper.createAccessToken(jwtHelper.decryptToken(refreshToken));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(new JwtDTO(accessToken, refreshToken), headers, HttpStatus.OK);
    }
}
