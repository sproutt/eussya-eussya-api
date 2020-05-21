package com.sproutt.eussyaeussyaapi.api.phrase;

import com.sproutt.eussyaeussyaapi.api.phrase.dto.PhraseResponseDTO;
import com.sproutt.eussyaeussyaapi.application.phrase.PhraseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PhraseController {

    private final PhraseService phraseService;

    @GetMapping("/phrase")
    public ResponseEntity<PhraseResponseDTO> getRandomPhrase() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(phraseService.getRandomPhrase(), headers, HttpStatus.OK);
    }
}
