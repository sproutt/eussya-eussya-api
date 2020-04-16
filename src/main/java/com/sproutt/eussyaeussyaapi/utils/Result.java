package com.sproutt.eussyaeussyaapi.utils;

import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor
public class Result {

    public static ResponseEntity created() {

        return ResponseEntity.status(HttpStatus.CREATED)
                             .contentType(MediaType.APPLICATION_JSON)
                             .build();
    }

    public static ResponseEntity ok() {

        return ResponseEntity.ok()
                             .contentType(MediaType.APPLICATION_JSON)
                             .build();
    }

    public static ResponseEntity okWithToken(String tokenKey, String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(tokenKey, token);

        return new ResponseEntity(headers, HttpStatus.OK);
    }

    public static <T> ResponseEntity<T> badRequest(T message) {
        return ResponseEntity.badRequest()
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(message);
    }

    public static <T> ResponseEntity<T> notFound(T message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND.value())
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(message);
    }
}
