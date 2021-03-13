package com.sproutt.eussyaeussyaapi.api.file;

import com.drew.imaging.ImageProcessingException;
import com.sproutt.eussyaeussyaapi.api.aspect.member.LoginMember;
import com.sproutt.eussyaeussyaapi.api.file.dto.FileDto;
import com.sproutt.eussyaeussyaapi.api.member.dto.MemberTokenCommand;
import com.sproutt.eussyaeussyaapi.application.file.FileService;
import com.sproutt.eussyaeussyaapi.application.file.S3Service;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.domain.file.File;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(description = "으쌰으쌰 파일 관련 API", tags = {"Member - 담당자 : 김민섭"})
public class FileController {
    private static final String defaultProfileKey = "https://dugjnp7kky4tj.cloudfront.net/default_profile.jpg";
    private final S3Service s3Service;
    private final FileService fileService;
    private final MemberService memberService;

    @PostMapping("/profile")
    public ResponseEntity uploadProfile(@RequestHeader HttpHeaders requestHeaders,
                                        @LoginMember MemberTokenCommand memberTokenCommand,
                                        @RequestParam("file") MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty())
            return new ResponseEntity(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        String fileType = s3Service.getContentType(multipartFile.getOriginalFilename());
        if (!((fileType.equals("jpg") ||
                fileType.equals("jpeg") ||
                fileType.equals("png")))) {
            return new ResponseEntity(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        Member loginMember = memberService.findTokenOwner(memberTokenCommand);
        String fileKey = s3Service.upload(loginMember.getNickName(), multipartFile, fileType);
        fileService.saveProfile(loginMember, fileKey);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity(headers, HttpStatus.OK);
    }

    @GetMapping("/profile/{nickName}")
    public ResponseEntity<FileDto> getProfile(@PathVariable String nickName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (fileService.findByNickName(nickName).isEmpty()) {
            return new ResponseEntity<>(new FileDto(nickName, defaultProfileKey), headers, HttpStatus.OK);
        }
        File file = fileService.findByNickName(nickName).get();
        return new ResponseEntity<>(new FileDto(nickName, file.getStoragePath()), headers, HttpStatus.OK);
    }
}