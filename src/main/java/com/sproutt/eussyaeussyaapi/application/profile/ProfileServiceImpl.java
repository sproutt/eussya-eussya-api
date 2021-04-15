package com.sproutt.eussyaeussyaapi.application.profile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.ProfileContentType;
import com.sproutt.eussyaeussyaapi.utils.ImageResizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class ProfileServiceImpl implements ProfileService {
    private final AmazonS3 s3Client;
    private final String bucket;
    private final String cloudFrontDomain;
    private final String defaultProfilePath;

    private static final String INFIX = "_profile.";

    public ProfileServiceImpl(AmazonS3 s3Client,
                              @Value("${cloud.aws.s3.bucket}") String bucket,
                              @Value("${cloud.aws.cloudFront.domain}") String cloudFrontDomain,
                              @Value("${cloud.aws.s3.profile.default}") String defaultProfilePath) {
        this.s3Client = s3Client;
        this.bucket = bucket;
        this.cloudFrontDomain = cloudFrontDomain;
        this.defaultProfilePath = defaultProfilePath;
    }

    @Override
    public String uploadProfile(Member loginMember, MultipartFile file) throws IOException {
        String loginMemberProfilePath = loginMember.getProfilePath();
        if (!loginMemberProfilePath.equals(defaultProfilePath)) {
            String profileKey = getProfileKey(loginMemberProfilePath);
            if (s3Client.doesObjectExist(bucket, profileKey)) {
                s3Client.deleteObject(bucket, profileKey);
            }
        }
        String postFix = "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String contentType = getContentType(file.getOriginalFilename());
        String newFileKey = loginMember.getNickName() + INFIX + contentType + postFix;
        s3Client.putObject(new PutObjectRequest(bucket, newFileKey, ImageResizer.resizeImage(file, contentType).getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return cloudFrontDomain + newFileKey;
    }

    @Override
    public boolean isImageType(String fileName) {
        return ProfileContentType.isImageType(getContentType(fileName));
    }

    private String getContentType(String filePath) {
        String[] splitFilePath = filePath.split("\\.");
        int contentTypeIndex = splitFilePath.length - 1;
        return splitFilePath[contentTypeIndex];
    }

    private String getProfileKey(String profilePath) {
        String[] splitProfilePath = profilePath.split("/");
        int profileKeyIndex = splitProfilePath.length - 1;
        return splitProfilePath[profileKeyIndex];
    }
}