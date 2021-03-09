package com.sproutt.eussyaeussyaapi.application.file;

import com.sproutt.eussyaeussyaapi.domain.file.File;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface FileService {

    Optional<File> findByNickName(String nickName);

    void saveProfile(Member loginMember, String storagePath);
}
