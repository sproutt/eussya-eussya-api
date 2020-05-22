package com.sproutt.eussyaeussyaapi.api.datetime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;

@RestController
public class DateTimeController {
    @GetMapping("/time/now")
    public ResponseEntity<LocalDateTime> getNowTime() {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        LocalDateTime localDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        return new ResponseEntity<>(localDateTime, headers, HttpStatus.OK);
    }
}
