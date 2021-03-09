package com.sproutt.eussyaeussyaapi.application.file;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class S3Service {
    private String infix = "_profile.";
    public AmazonS3 s3Client;

    private final FileService fileService;

    @Value("${cloud.aws.credentials.accessKey}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secretKey}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    public String upload(String nickName, MultipartFile file) throws IOException {
        if(fileService.findByNickName(nickName).isPresent()) {
            String originFileKey = fileService.findByNickName(nickName).get().getFileKey();
            if (s3Client.doesObjectExist(bucket, originFileKey)) {
                s3Client.deleteObject(bucket, originFileKey);
            }
        }
        SimpleDateFormat date = new SimpleDateFormat("yyyymmddHHmmss");
        String newFileKey = nickName + infix + getContentType(file.getOriginalFilename()) + "-" + date.format(new Date());
        s3Client.putObject(new PutObjectRequest(bucket, newFileKey, file.getInputStream(), null)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return newFileKey;
    }

    private String getContentType(String filePath) {
        String[] strings = filePath.split("\\.");
        return strings[strings.length - 1];
    }
}
