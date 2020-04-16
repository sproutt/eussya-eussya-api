package com.sproutt.eussyaeussyaapi.api.member;

import com.sproutt.eussyaeussyaapi.api.member.dto.JoinDTO;
import com.sproutt.eussyaeussyaapi.api.member.dto.LoginDTO;
import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.DuplicationMemberException;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.DuplicationNickNameException;
import com.sproutt.eussyaeussyaapi.utils.Result;
import io.swagger.annotations.Api;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Api(description = "으쌰으쌰 회원 관련 API", tags = {"Member - 담당자 : 김종근"})
public class MemberController {

    @Value("${token.key}")
    private String TOKEN_KEY;

    private final MemberService memberService;
    private final JwtHelper jwtHelper;

    public MemberController(MemberService memberService, JwtHelper jwtHelper) {
        this.memberService = memberService;
        this.jwtHelper = jwtHelper;
    }

    @PostMapping("/members")
    public ResponseEntity createMemberWithLocalProvider(@Valid @RequestBody JoinDTO joinDTO) {

        memberService.joinWithLocalProvider(joinDTO);

        return Result.created();
    }

    @PostMapping("/login")
    public ResponseEntity loginMember(@Valid @RequestBody LoginDTO loginDTO) {
        Member loginMember = memberService.login(loginDTO);

        String token = jwtHelper.createToken(loginMember);

        return Result.okWithToken(TOKEN_KEY, token);
    }

    @PostMapping("/members/{memberId}/authcode")
    public ResponseEntity sendAuthCodeEmail(@PathVariable String memberId) {

        memberService.sendAuthCodeToEmail(memberId);

        return Result.ok();
    }

    @PostMapping("/email-auth")
    public ResponseEntity authenticateEmail(@Valid @RequestBody EmailAuthDTO emailAuthDTO) {

        memberService.authenticateEmail(emailAuthDTO);

        return Result.ok();
    }

    @GetMapping("/members/validate/memberid/{memberId}")
    public ResponseEntity checkMemberIdDuplication(@PathVariable String memberId) {

        if (memberService.isDuplicatedMemberId(memberId)) {
            throw new DuplicationMemberException();
        }

        return Result.ok();
    }

    @GetMapping("/members/validate/nickname/{nickName}")
    public ResponseEntity checkNickNameDuplication(@PathVariable String nickName) {

        if (memberService.isDuplicatedNickName(nickName)) {
            throw new DuplicationNickNameException();
        }

        return Result.ok();
    }
}
