package com.sproutt.eussyaeussyaapi.api.member;

import com.sproutt.eussyaeussyaapi.api.aspect.member.LoginMember;
import com.sproutt.eussyaeussyaapi.api.member.dto.MemberTokenCommand;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.application.profile.ProfileService;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
@Api(description = "으쌰으쌰 프로필 관련 API", tags = {"Profile - 담당자 : 김민섭"})
public class ProfileController {
    private final ProfileService profileService;
    private final MemberService memberService;

    @ApiOperation(value = "프로필 업로드 확인")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Success Upload Profile"),
            @ApiResponse(code = 400, message = "Request Error")
    })
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

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @ApiOperation(value = "기본 프로필 이미지로 변경 확인")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success Reset Profile"),
            @ApiResponse(code = 400, message = "Request Error")
    })
    @PutMapping("/profile")
    public ResponseEntity resetProfile(@RequestHeader HttpHeaders requestHeaders,
                                       @LoginMember MemberTokenCommand memberTokenCommand) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Member loginMember = memberService.findTokenOwner(memberTokenCommand);
        String defaultProfilePath = profileService.resetProfile(loginMember);
        memberService.updateProfilePath(loginMember, defaultProfilePath);

        return new ResponseEntity(headers, HttpStatus.OK);
    }
}