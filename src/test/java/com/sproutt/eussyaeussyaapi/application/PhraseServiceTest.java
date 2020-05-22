package com.sproutt.eussyaeussyaapi.application;

import com.sproutt.eussyaeussyaapi.api.phrase.dto.PhraseResponseDTO;
import com.sproutt.eussyaeussyaapi.application.phrase.PhraseService;
import com.sproutt.eussyaeussyaapi.application.phrase.PhraseServiceImpl;
import com.sproutt.eussyaeussyaapi.domain.phrase.Phrase;
import com.sproutt.eussyaeussyaapi.domain.phrase.PhraseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PhraseServiceTest {

    private PhraseRepository phraseRepository = mock(PhraseRepository.class);
    private PhraseService phraseService;

    @BeforeEach
    void setUp() {
        phraseService = new PhraseServiceImpl(phraseRepository);
    }

    @Test
    @DisplayName("동기부여 글귀 db에서 랜덤으로 하나 가져오기")
    void getRandomPhrase() {
        Phrase phrase = new Phrase(1l, "this is test text");
        when(phraseRepository.findRandomPhrase()).thenReturn(Optional.of(phrase));

        PhraseResponseDTO phraseDTO = phraseService.getRandomPhrase();

        assertEquals("this is test text", phraseDTO.getText());
    }
}
