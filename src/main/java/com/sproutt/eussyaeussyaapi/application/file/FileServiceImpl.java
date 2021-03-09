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
    public static final String CLOUD_FRONT_DOMAIN_NAME = "dugjnp7kky4tj.cloudfront.net";

    private final FileRepository fileRepository;

    @Override
    public Optional<File> findByNickName(String nickName) {
        return fileRepository.findByNickName(nickName);
    }

    @Override
    public void saveProfile(Member loginMember, String fileKey) {
        String storagePath = "https://" + CLOUD_FRONT_DOMAIN_NAME + "/" + fileKey;
        if (!fileRepository.findByNickName(loginMember.getNickName()).isPresent()) {
            fileRepository.save(File.builder()
                    .member(loginMember)
                    .storagePath(storagePath)
                    .fileKey(fileKey)
                    .build());
        } else {
            File file = fileRepository.findByNickName(loginMember.getNickName()).get();
            fileRepository.save(file.updateFile(storagePath, fileKey));
        }
    }
}
