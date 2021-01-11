package com.sproutt.eussyaeussyaapi.api.member;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sproutt.eussyaeussyaapi.api.config.JasyptConfig;
import com.sproutt.eussyaeussyaapi.api.member.dto.MemberJoinCommand;
import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.DuplicationMemberException;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MemberController.class, excludeFilters = @ComponentScan.Filter(EnableWebSecurity.class))
@Import(JasyptConfig.class)
@WithMockUser("fake_user")
public class MemberControllerTest {

    private static final String DEFAULT_MEMBER_ID = "kjkun7631@naver.com";
    private static final String DEFAULT_PASSWORD = "12345aA!";
    private static final String DEFAULT_NAME = "test";

    private MockMvc mvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtHelper jwtHelper;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        this.mvc = MockMvcBuilders.webAppContextSetup(context)
                                  .apply(springSecurity())
                                  .build();
    }

    @Test
    @DisplayName("회원가입 테스트(올바른 요청일 경우)")
    public void createMemberWithLocalProvider() throws Exception {
        MemberJoinCommand memberJoinCommand = defaultSignUpDTO();
        Member member = MemberFactory.getDefaultMember();

        given(memberService.joinWithLocalProvider(memberJoinCommand)).willReturn(member);

        ResultActions actions = mvc.perform(post("/members")
                .with(csrf())
                .content(asJsonString(memberJoinCommand))
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                                   .andDo(print());

        actions
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("회원가입 테스트(중복된 memberId를 입력한 경우)")
    public void createMember_with_exist_memberId() throws Exception {
        MemberJoinCommand memberJoinCommand = defaultSignUpDTO();

        given(memberService.joinWithLocalProvider(memberJoinCommand)).willThrow(new DuplicationMemberException());

        ResultActions actions = mvc.perform(post("/members")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)).andDo(print());

        actions
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("인증 코드 메일 전송 테스트")
    public void send_authCode_to_email() throws Exception {
        Member member = MemberFactory.getDefaultMember();
        String email = member.getMemberId();

        given(memberService.sendAuthCodeToEmail(email)).willReturn(member);

        ResultActions actions = mvc.perform(post("/members/" + email + "/authcode")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                                   .andDo(print());

        actions
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("이메일 인증 테스트")
    public void authenticateEmail() throws Exception {
        Member member = MemberFactory.getDefaultMember();

        EmailAuthCommand emailAuthCommand = EmailAuthCommand.builder()
                                                            .memberId(member.getMemberId())
                                                            .authCode(member.getAuthentication())
                                                            .build();

        when(memberService.authenticateEmail(emailAuthCommand)).thenReturn(member);

        ResultActions actions = mvc.perform(post("/email-auth")
                .with(csrf())
                .content(asJsonString(emailAuthCommand))
                .contentType(MediaType.APPLICATION_JSON))
                                   .andDo(print());

        actions
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("memberId 중복 체크(중복되지 않은 경우)")
    void checkDuplicatedMemberId_when_not_exist() throws Exception {
        Member member = MemberFactory.getDefaultMember();

        when(memberService.isDuplicatedMemberId(member.getMemberId())).thenReturn(false);

        ResultActions actions = mvc.perform(get("/members/validate/memberid/{memberId}", member.getMemberId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                                   .andDo(print());

        actions
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("memberId 중복 체크(중복된 경우)")
    public void checkDuplicatedMemberId_when_exist() throws Exception {
        Member member = MemberFactory.getDefaultMember();

        when(memberService.isDuplicatedMemberId(member.getMemberId())).thenReturn(true);

        ResultActions actions = mvc.perform(get("/members/validate/memberid/{memberId}", member.getMemberId())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                                   .andDo(print());

        actions
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("nickName 중복 체크 체크(중복되지 않은 경우)")
    public void checkDuplicatedNickName_when_not_exist() throws Exception {
        Member member = MemberFactory.getDefaultMember();

        when(memberService.isDuplicatedNickName(member.getNickName())).thenReturn(false);

        ResultActions actions = mvc.perform(get("/members/validate/nickname/{nickName}", member.getNickName())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                                   .andDo(print());

        actions
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("nickName 중복 체크 체크(중복된 경우)")
    public void checkDuplicatedNickName_when_exist() throws Exception {
        Member member = MemberFactory.getDefaultMember();

        when(memberService.isDuplicatedNickName(member.getNickName())).thenReturn(true);

        ResultActions actions = mvc.perform(get("/members/validate/nickname/{nickName}", member.getNickName())
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
                                   .andDo(print());

        actions
                .andExpect(status().isBadRequest());
    }

    private static String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private MemberJoinCommand defaultSignUpDTO() {
        return MemberJoinCommand.builder()
                                .memberId(DEFAULT_MEMBER_ID)
                                .password(DEFAULT_PASSWORD)
                                .nickName(DEFAULT_NAME)
                                .build();
    }
}