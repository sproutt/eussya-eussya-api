package com.sproutt.eussyaeussyaapi.api.member;

import com.sproutt.eussyaeussyaapi.api.aspect.member.LoginMember;
import com.sproutt.eussyaeussyaapi.api.member.dto.MemberTokenCommand;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.application.profile.ProfileService;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(description = "으쌰으쌰 파일 관련 API", tags = {"Member - 담당자 : 김민섭"})
public class ProfileController {
    private final ProfileService profileService;
    private final MemberService memberService;

    @PostMapping("/profile")
    public ResponseEntity uploadProfile(@RequestHeader HttpHeaders requestHeaders,
                                        @LoginMember MemberTokenCommand memberTokenCommand,
                                        @RequestParam("file") MultipartFile multipartFile) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        if (multipartFile.isEmpty()) {
            return new ResponseEntity(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        if (!(profileService.isImageType(multipartFile.getOriginalFilename()))) {
            return new ResponseEntity(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        Member loginMember = memberService.findTokenOwner(memberTokenCommand);
        String profilePath = profileService.uploadProfile(loginMember, multipartFile);
        memberService.updateProfilePath(loginMember, profilePath);

        return new ResponseEntity(headers, HttpStatus.OK);
    }
}