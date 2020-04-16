package com.sproutt.eussyaeussyaapi.utils;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor
public class Result<T> {

    public static <T> ResponseEntity<T> created() {

        return new ResponseEntity<T>(getHeaders(), HttpStatus.CREATED);
    }

    public static <T> ResponseEntity<T> ok() {

        return new ResponseEntity<T>(getHeaders(), HttpStatus.OK);
    }

    public static <T> ResponseEntity<T> okWithToken(String tokenKey, String token) {
        HttpHeaders headers = getHeaders();
        headers.set(tokenKey, token);

        return new ResponseEntity<T>(headers, HttpStatus.OK);
    }

    private static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return headers;
    }
}
