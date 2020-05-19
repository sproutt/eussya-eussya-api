package com.sproutt.eussyaeussyaapi.application.phrase;


import com.sproutt.eussyaeussyaapi.api.phrase.dto.PhraseResponseDTO;
import com.sproutt.eussyaeussyaapi.domain.phrase.Phrase;
import com.sproutt.eussyaeussyaapi.domain.phrase.PhraseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PhraseServiceImpl implements PhraseService {

    private final PhraseRepository phraseRepository;

    @Override
    public PhraseResponseDTO getRandomPhrase() {

        Phrase phrase = phraseRepository.findRandomPhrase().orElseThrow(RuntimeException::new);

        return phrase.toResponseDTO();
    }
}
