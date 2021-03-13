package com.sproutt.eussyaeussyaapi.application;

import com.sproutt.eussyaeussyaapi.application.file.FileService;
import com.sproutt.eussyaeussyaapi.application.file.FileServiceImpl;
import com.sproutt.eussyaeussyaapi.domain.file.File;
import com.sproutt.eussyaeussyaapi.domain.file.FileRepository;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.sproutt.eussyaeussyaapi.application.file.FileServiceImpl.CLOUD_FRONT_DOMAIN_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FileServiceTest {
    private FileService fileService;
    private Member loginMember;

    @Mock
    private FileRepository fileRepository;

    @BeforeEach
    void setUp() {
        fileService = new FileServiceImpl(fileRepository);
        loginMember = MemberFactory.getDefaultMember();
    }

    @Test
    @DisplayName("닉네임으로 파일 찾기")
    void findByNickNameTest() {
        //given
        String tmpNickName = "kiki";
        String tmpStoragePath = "https://dugjnp7kky4tj.cloudfront.net/test_profile.jpg";
        String tmpFileKey = "test_profile.jpg";
        File file = File.builder()
                .member(loginMember)
                .storagePath(tmpStoragePath)
                .fileKey(tmpFileKey)
                .build();
        //when
        when(fileRepository.findByNickName(any())).thenReturn(Optional.of(file));
        File searchedFile = fileService.findByNickName(tmpNickName).get();
        //then
        assertEquals(searchedFile.getNickName(), file.getNickName());
        assertEquals(searchedFile.getStoragePath(), file.getStoragePath());
        assertEquals(searchedFile.getFileKey(), file.getFileKey());
    }

    @Test
    @DisplayName("프로필 등록 테스트")
    void saveProfileTest() {
        String tmpFileKey = "test_profile.jpg";
        String tmpStoragePath = CLOUD_FRONT_DOMAIN_NAME + tmpFileKey;
        File file = File.builder()
                .fileKey(tmpFileKey)
                .storagePath(tmpStoragePath)
                .member(loginMember)
                .build();

        when(fileRepository.save(any(File.class))).thenReturn(file);
        File savedFile = fileService.saveProfile(loginMember, tmpStoragePath);

        assertEquals(savedFile.getStoragePath(), file.getStoragePath());
    }
}

