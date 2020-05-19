package com.sproutt.eussyaeussyaapi.domain.phrase;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PhraseRepository extends JpaRepository<Phrase, Long> {

    @Query(value = "SELECT * FROM Phrase ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<Phrase> findRandomPhrase();
}
