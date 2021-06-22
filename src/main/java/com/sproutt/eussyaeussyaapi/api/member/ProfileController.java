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
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(description = "으쌰으쌰 프로필 관련 API", tags = {"Profile - 담당자 : 김민섭"})
public class ProfileController {
    private final ProfileService profileService;
    private final MemberService memberService;

    @ApiOperation(value = "프로필 경로 확인")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success Get Profile"),
            @ApiResponse(code = 400, message = "Request Error")
    })
    @GetMapping("/members/{id}/profile")
    public ResponseEntity getProfile(@PathVariable Long id,
                                     @RequestHeader HttpHeaders requestHeaders) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, String> data = new HashMap<>();
        data.put("profilePath", memberService.getProfilePathById(id));
        return new ResponseEntity(data, headers, HttpStatus.OK);
    }

    @ApiOperation(value = "프로필 업로드 확인")
    @ApiResponses({
            @ApiResponse(code = 201, message = "Success Upload Profile"),
            @ApiResponse(code = 400, message = "Request Error")
    })

    @PostMapping("/members/{id}/profile")
    public ResponseEntity uploadProfile(@PathVariable Long id,
                                        @RequestHeader HttpHeaders requestHeaders,
                                        @LoginMember MemberTokenCommand memberTokenCommand,
                                        @RequestParam("file") MultipartFile multipartFile) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Member loginMember = memberService.findTokenOwner(memberTokenCommand);
        if (!memberService.isSameUser(loginMember, id)) {
            return new ResponseEntity(headers, HttpStatus.FORBIDDEN);
        }
        if (multipartFile.isEmpty() || !(profileService.isImageType(multipartFile.getOriginalFilename()))) {
            return new ResponseEntity(new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        String profilePath = profileService.uploadProfile(loginMember, multipartFile);
        memberService.updateProfilePath(loginMember, profilePath);

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @ApiOperation(value = "기본 프로필 이미지로 변경 확인")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Success Reset Profile"),
            @ApiResponse(code = 400, message = "Request Error")
    })
    @PutMapping("/members/{id}/profile")
    public ResponseEntity resetProfile(@PathVariable Long id,
                                       @RequestHeader HttpHeaders requestHeaders,
                                       @LoginMember MemberTokenCommand memberTokenCommand) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Member loginMember = memberService.findTokenOwner(memberTokenCommand);
        if (!memberService.isSameUser(loginMember, id)) {
            return new ResponseEntity(headers, HttpStatus.FORBIDDEN);
        }
        String defaultProfilePath = profileService.resetProfile(loginMember);
        memberService.updateProfilePath(loginMember, defaultProfilePath);

        return new ResponseEntity(headers, HttpStatus.OK);
    }
}