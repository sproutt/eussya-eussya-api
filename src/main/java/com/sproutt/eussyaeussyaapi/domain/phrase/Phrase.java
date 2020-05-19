package com.sproutt.eussyaeussyaapi.domain.phrase;

import com.sproutt.eussyaeussyaapi.api.phrase.dto.PhraseResponseDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Phrase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String text;

    public PhraseResponseDTO toResponseDTO() {
        return new PhraseResponseDTO(this.text);
    }
}
