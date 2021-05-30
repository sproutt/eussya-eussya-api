package com.sproutt.eussyaeussyaapi.api.member;

import com.sproutt.eussyaeussyaapi.api.member.dto.MemberJoinCommand;
import com.sproutt.eussyaeussyaapi.api.member.dto.MemberLoginCommand;
import com.sproutt.eussyaeussyaapi.api.member.dto.MemberDTO;
import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.api.security.dto.JwtDTO;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.DuplicationMemberException;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.DuplicationNickNameException;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Api(description = "으쌰으쌰 회원 관련 API", tags = {"Member - 담당자 : 김종근"})
public class MemberController {

    private final MemberService memberService;
    private final JwtHelper jwtHelper;

    public MemberController(MemberService memberService, JwtHelper jwtHelper) {
        this.memberService = memberService;
        this.jwtHelper = jwtHelper;
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberDTO>> getMembers(@RequestParam(name = "exclude", required = false) String memberId) throws MessagingException {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        List<Member> memberList = memberService.findAllExclude(memberId);
        List<MemberDTO> memberDTOList = new ArrayList<>();
        memberList.forEach(member -> memberDTOList.add(member.toDTO()));

        return new ResponseEntity<>(memberDTOList, headers, HttpStatus.OK);
    }

    @PostMapping("/members")
    public ResponseEntity createMemberWithLocalProvider(@Valid @RequestBody MemberJoinCommand memberJoinCommand) throws MessagingException {

        memberService.joinWithLocalProvider(memberJoinCommand);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtDTO> loginMember(@Valid @RequestBody MemberLoginCommand memberLoginCommand) {
        Member loginMember = memberService.login(memberLoginCommand);

        String accessToken = jwtHelper.createAccessToken(loginMember.toJwtInfo());
        String refreshToken = jwtHelper.createRefreshToken(loginMember.toJwtInfo());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity<>(new JwtDTO(accessToken, refreshToken), headers, HttpStatus.OK);
    }

    @PostMapping("/members/{memberId}/authcode")
    public ResponseEntity sendAuthCodeEmail(@PathVariable String memberId) throws MessagingException {

        memberService.sendAuthCodeToEmail(memberId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return new ResponseEntity(headers, HttpStatus.OK);
    }

    @PostMapping("/email-auth")
    public ResponseEntity<String> authenticateEmail(@Valid @RequestBody EmailAuthCommand emailAuthCommand) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        memberService.authenticateEmail(emailAuthCommand);

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
