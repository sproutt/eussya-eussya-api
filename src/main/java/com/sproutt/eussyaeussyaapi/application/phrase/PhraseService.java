package com.sproutt.eussyaeussyaapi.application.phrase;

import com.sproutt.eussyaeussyaapi.api.phrase.dto.PhraseResponseDTO;
import com.sproutt.eussyaeussyaapi.domain.phrase.Phrase;

public interface PhraseService {

    PhraseResponseDTO getRandomPhrase();

    Phrase create(String phraseText);
}
