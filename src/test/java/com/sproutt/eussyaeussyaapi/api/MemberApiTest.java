package com.sproutt.eussyaeussyaapi.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.sproutt.eussyaeussyaapi.api.exceptions.DuplicatedMemberIdException;
import com.sproutt.eussyaeussyaapi.application.MemberService;
import com.sproutt.eussyaeussyaapi.domain.Member;
import com.sproutt.eussyaeussyaapi.domain.dto.JoinDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(MemberController.class)
public class MemberApiTest {
    private static final String DEFAULT_MEMBER_ID = "test@gmail.com";
    private static final String DEFAULT_PASSWORD = "1111";
    private static final String DEFAULT_NAME = "test";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private MemberService memberService;

    @Test
    public void createMember() throws Exception {
        JoinDTO joinDTO = defaultSignUpDTO();
        Member member = joinDTO.toEntity();

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

        given(memberService.join(joinDTO)).willThrow(new DuplicatedMemberIdException());

        ResultActions actions = mvc.perform(post("/members", joinDTO)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print());

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

    private JoinDTO defaultSignUpDTO() {
        return JoinDTO.builder()
                .memberId(DEFAULT_MEMBER_ID)
                .password(DEFAULT_PASSWORD)
                .name(DEFAULT_NAME)
                .build();
    }
}
