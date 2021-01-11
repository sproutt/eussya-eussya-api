package com.sproutt.eussyaeussyaapi.api;

import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.time.ZonedDateTime;

public class HeaderSetUpWithToken {

    public HttpHeaders setUpHeader() {
        String token = "token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(JwtHelper.ACCESS_TOKEN_HEADER, token);
        headers.setZonedDateTime("date", ZonedDateTime.now());

        return headers;
    }
}
