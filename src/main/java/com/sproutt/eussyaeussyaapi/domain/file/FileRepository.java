package com.sproutt.eussyaeussyaapi.domain.file;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository  extends JpaRepository<File,Long> {
    Optional<File> findByNickName(String nickName);
}
