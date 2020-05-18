package com.sproutt.eussyaeussyaapi.api.member;

import com.sproutt.eussyaeussyaapi.api.member.dto.JoinDTO;
import com.sproutt.eussyaeussyaapi.api.member.dto.LoginDTO;
import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.DuplicationMemberException;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.DuplicationNickNameException;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@Api(description = "으쌰으쌰 회원 관련 API", tags = {"Member - 담당자 : 김종근"})
public class MemberController {

    @Value("${jwt.header}")
    private String tokenKey;

    @Value("${jwt.secret}")
    private String secretKey;

    private final MemberService memberService;
    private final JwtHelper jwtHelper;

    public MemberController(MemberService memberService, JwtHelper jwtHelper) {
        this.memberService = memberService;
        this.jwtHelper = jwtHelper;
    }

    @PostMapping("/members")
    public ResponseEntity createMemberWithLocalProvider(@Valid @RequestBody JoinDTO joinDTO) {

        memberService.joinWithLocalProvider(joinDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity loginMember(@Valid @RequestBody LoginDTO loginDTO) {
        Member loginMember = memberService.login(loginDTO);

        String token = jwtHelper.createToken(secretKey, loginMember.toJwtInfo());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(tokenKey, token);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PostMapping("/members/{memberId}/authcode")
    public ResponseEntity sendAuthCodeEmail(@PathVariable String memberId) {

        memberService.sendAuthCodeToEmail(memberId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity(headers, HttpStatus.OK);
    }

    @PostMapping("/email-auth")
    public ResponseEntity<String> authenticateEmail(@Valid @RequestBody EmailAuthDTO emailAuthDTO) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        memberService.authenticateEmail(emailAuthDTO);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @GetMapping("/members/validate/memberid/{memberId}")
    public ResponseEntity checkMemberIdDuplication(@PathVariable String memberId) {

        if (memberService.isDuplicatedMemberId(memberId)) {
            throw new DuplicationMemberException();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity(headers, HttpStatus.OK);
    }

    @GetMapping("/members/validate/nickname/{nickName}")
    public ResponseEntity checkNickNameDuplication(@PathVariable String nickName) {

        if (memberService.isDuplicatedNickName(nickName)) {
            throw new DuplicationNickNameException();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity(headers, HttpStatus.OK);
    }
}
