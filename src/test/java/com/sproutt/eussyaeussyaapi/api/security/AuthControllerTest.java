package com.sproutt.eussyaeussyaapi.api.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = AuthController.class, includeFilters = @ComponentScan.Filter(classes = {Configuration.class}))
public class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private JwtHelper jwtHelper;

    @Test
    @DisplayName("Refresh Token을 통해 새로운 Access Token을 발급 테스트")
    void issue_new_access_token() throws Exception {
        given(jwtHelper.isUsable(any(), any())).willReturn(true);

        ResultActions actions = mvc.perform(post("/auth/accesstoken")
                .header("Refresh-Authorization", "testtoken")
                .contentType(MediaType.APPLICATION_JSON)).andDo(print());

        actions
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Refresh Token이 유효하지 않을 경우 발급 거부 테스트")
    void invalid_refresh_token_test() throws Exception {
        given(jwtHelper.isUsable(any(), any())).willReturn(false);

        ResultActions actions = mvc.perform(post("/auth/accesstoken")
                .header("Refresh-Authorization", "invalidtoken")
                .contentType(MediaType.APPLICATION_JSON)).andDo(print());

        actions
                .andExpect(status().isBadRequest());
    }
}
