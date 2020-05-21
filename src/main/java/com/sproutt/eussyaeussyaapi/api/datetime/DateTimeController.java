package com.sproutt.eussyaeussyaapi.api.datetime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@RestController
public class DateTimeController {

    @Value("${jwt.header}")
    private String tokenKey;

    @GetMapping("/time/now")
    public ResponseEntity<LocalDateTime> getNowTime(@RequestHeader HttpHeaders requestHeaders) {
        List<String> token = requestHeaders.get(tokenKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(tokenKey, token.get(0));

        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        return new ResponseEntity<>(localDateTime, headers, HttpStatus.OK);
    }
}
