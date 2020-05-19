package com.sproutt.eussyaeussyaapi.api.phrase;

import com.sproutt.eussyaeussyaapi.api.phrase.dto.PhraseResponseDTO;
import com.sproutt.eussyaeussyaapi.application.phrase.PhraseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PhraseController {

    @Value("${jwt.header}")
    private String tokenKey;

    private final PhraseService phraseService;

    @GetMapping("/phrase")
    public ResponseEntity<PhraseResponseDTO> getRandomPhrase(@RequestHeader HttpHeaders requestHeaders) {
        List<String> token = requestHeaders.get(tokenKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(tokenKey, token.get(0));

        return new ResponseEntity<>(phraseService.getRandomPhrase(), headers, HttpStatus.OK);
    }
}
