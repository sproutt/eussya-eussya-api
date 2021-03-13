package com.sproutt.eussyaeussyaapi.application.file;

import com.sproutt.eussyaeussyaapi.domain.file.File;
import com.sproutt.eussyaeussyaapi.domain.file.FileRepository;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    public static final String CLOUD_FRONT_DOMAIN_NAME = "https://dugjnp7kky4tj.cloudfront.net/";

    private final FileRepository fileRepository;

    @Override
    public Optional<File> findByNickName(String nickName) {
        return fileRepository.findByNickName(nickName);
    }

    @Override
    public File saveProfile(Member loginMember, String fileKey) {
        String storagePath = CLOUD_FRONT_DOMAIN_NAME + fileKey;
        if (!fileRepository.findByNickName(loginMember.getNickName()).isPresent()) {
            return fileRepository.save(File.builder()
                    .member(loginMember)
                    .storagePath(storagePath)
                    .fileKey(fileKey)
                    .build());
        } else {
            File file = fileRepository.findByNickName(loginMember.getNickName()).get();
            return fileRepository.save(file.updateFile(storagePath, fileKey));
        }
    }
}
