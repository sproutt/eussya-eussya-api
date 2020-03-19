package com.sproutt.eussyaeussyaapi.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sproutt.eussyaeussyaapi.api.dto.EmailDTO;
import com.sproutt.eussyaeussyaapi.api.member.MemberController;
import com.sproutt.eussyaeussyaapi.api.member.dto.JoinDTO;
import com.sproutt.eussyaeussyaapi.api.security.JwtService;
import com.sproutt.eussyaeussyaapi.application.MailService;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.domain.exceptions.DuplicationMemberException;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.utils.RandomGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.PersistenceException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MemberController.class)
public class MemberControllerTest {
    private static final String DEFAULT_MEMBER_ID = "test@gmail.com";
    private static final String DEFAULT_PASSWORD = "12345aA!";
    private static final String DEFAULT_NAME = "test";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private MailService mailService;



    @Test
    public void createMember() throws Exception {
        JoinDTO joinDTO = defaultSignUpDTO();
        Member member = defaultMember();

        given(memberService.join(joinDTO)).willReturn(member);

        ResultActions actions = mvc.perform(post("/members")
                .content(asJsonString(joinDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        actions
                .andExpect(status().isCreated());
    }

    @Test
    public void createMember_with_exist_memberId() throws Exception {
        JoinDTO joinDTO = defaultSignUpDTO();

        given(memberService.join(joinDTO)).willThrow(new DuplicationMemberException());

        ResultActions actions = mvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)).andDo(print());

        actions
                .andExpect(status().isBadRequest());
    }

    @Test
    void sendAuthEmail() throws Exception {
        String email = "kjkun7631@naver.com";
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setEmail(email);
        String authCode = RandomGenerator.createAuthenticationCode();

        given(mailService.sendAuthEmail(email)).willReturn(authCode);

        ResultActions actions = mvc.perform(post("/email-auth")
                .content(asJsonString(emailDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        actions
                .andExpect(status().isOk());
    }

    @Test
    void sendAuthEmail_with_wrongEmail() throws Exception {
        String email = "wrong";
        EmailDTO emailDTO = new EmailDTO();
        emailDTO.setEmail(email);

        when(mailService.sendAuthEmail(email)).thenThrow(new PersistenceException());

        ResultActions actions = mvc.perform(post("/email-auth")
                .content(asJsonString(emailDTO))
                .contentType(MediaType.APPLICATION_JSON)).andDo(print());

        actions
                .andExpect(status().isBadRequest());

        assertThrows(PersistenceException.class, () -> mailService.sendAuthEmail(email));
    }

    private static String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private JoinDTO defaultSignUpDTO() {
        return JoinDTO.builder()
                .memberId(DEFAULT_MEMBER_ID)
                .password(DEFAULT_PASSWORD)
                .nickName(DEFAULT_NAME)
                .build();
    }

    private Member defaultMember() {
        return Member.builder()
                .memberId(DEFAULT_MEMBER_ID)
                .password(DEFAULT_PASSWORD)
                .nickName(DEFAULT_NAME)
                .build();
    }
}
