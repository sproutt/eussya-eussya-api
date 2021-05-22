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

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {
    private ProfileService profileService;
    private Member loginMember;

    private static final String cloudFrontDomain = "https://d3kjmjnyg8cjdl.cloudfront.net/";
    private static final String defaultProfilePath = "https://d3kjmjnyg8cjdl.cloudfront.net/default_profile.jpg";
    private static final String bucket = "eussya-eussya-bucket";

    @Mock
    private AmazonS3 amazonS3;

    @BeforeEach
    void setUp() {
        profileService = new ProfileServiceImpl(amazonS3, bucket, cloudFrontDomain, defaultProfilePath);
        loginMember = MemberFactory.getDefaultMember();
        loginMember.saveProfilePath(defaultProfilePath);
    }

    @Test
    @DisplayName("프로필 등록을 하면 S3에 업로드한 URL이 반환된다")
    void saveProfileTest() throws IOException {
        URL url = Thread.currentThread().getContextClassLoader().getResource("default_profile.jpeg");
        Image tmpImage = ImageIO.read(url);

        BufferedImage bufferedImage = new BufferedImage(tmpImage.getWidth(null), tmpImage.getHeight(null), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.drawImage(tmpImage, 0, 0, null);
        graphics.dispose();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpeg", byteArrayOutputStream);

        MockMultipartFile file = new MockMultipartFile("default_profile", "default_profile.jpeg", "multipart/form-data", byteArrayOutputStream.toByteArray());
        String tmpProfilePath = cloudFrontDomain + loginMember.getNickName() + "_profile.jpeg";
        String newProfilePath = profileService.uploadProfile(loginMember, file);

        assertTrue(newProfilePath.contains(tmpProfilePath));
    }

    @Test
    @DisplayName("기본 프로필 경로를 반환한다.")
    void getDefaultProfilePath() {
        String tmpDefaultProfilePath = profileService.resetProfile(loginMember);

        assertTrue(defaultProfilePath.equals(tmpDefaultProfilePath));
    }
}
