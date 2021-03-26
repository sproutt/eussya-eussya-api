package com.sproutt.eussyaeussyaapi.application;

import com.amazonaws.services.s3.AmazonS3;
import com.sproutt.eussyaeussyaapi.application.profile.ProfileService;
import com.sproutt.eussyaeussyaapi.application.profile.ProfileServiceImpl;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {
    private ProfileService profileService;
    private Member loginMember;

    @Mock
    private AmazonS3 amazonS3;

    @BeforeEach
    void setUp() {
        profileService = new ProfileServiceImpl(amazonS3);
        ReflectionTestUtils.setField(profileService, "cloudFrontDomain", "https://dugjnp7kky4tj.cloudfront.net/");
        ReflectionTestUtils.setField(profileService, "defaultProfilePath", "https://dugjnp7kky4tj.cloudfront.net/default_profile.jpg");
        loginMember = MemberFactory.getDefaultMember();
    }

    @Test
    @DisplayName("프로필 등록을 하면 S3에 업로드한 URL이 반환된다")
    void saveProfileTest() throws IOException {
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "multipart/form-data", "Spring Framework".getBytes());
        String tmpProfilePath = "https://dugjnp7kky4tj.cloudfront.net/" + loginMember.getNickName() + "_profile.jpg";
        loginMember.saveProfilePath("https://dugjnp7kky4tj.cloudfront.net/default_profile.jpg");
        String newProfilePath = profileService.uploadProfile(loginMember, file);

        assertTrue(newProfilePath.contains(tmpProfilePath));
    }
}

