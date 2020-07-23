package com.sproutt.eussyaeussyaapi.api.oauth2;

import com.sproutt.eussyaeussyaapi.api.oauth2.exception.UnSupportedOAuth2Exception;
import com.sproutt.eussyaeussyaapi.api.oauth2.service.OAuth2RequestService;
import com.sproutt.eussyaeussyaapi.api.oauth2.service.SocialService;
import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(value = SocialController.class, includeFilters = @ComponentScan.Filter(classes = {Configuration.class}))
class SocialControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SocialService socialService;

    @MockBean
    private OAuth2RequestService oAuth2RequestService;

    @MockBean
    private JwtHelper jwtHelper;

    @DisplayName("github으로 로그인")
    @Test
    public void login_by_github() throws Exception {
        Member githubMember = MemberFactory.getGithubMember();

        when(oAuth2RequestService.getUserInfoByProvider(eq("token"), eq("github"), any()))
                .thenReturn(githubMember);
        when(socialService.login(githubMember)).thenReturn(githubMember);
        when(jwtHelper.createToken(any(), any())).thenReturn("token");

        ResultActions actions = mockMvc.perform(post("/social/login/github")
                .param("accessToken", "token")
                .contentType(MediaType.APPLICATION_JSON)).andDo(print());

        actions
                .andExpect(status().isOk());
    }

    @DisplayName("facebook으로 로그인")
    @Test
    public void login_by_facebook() throws Exception {
        Member githubMember = MemberFactory.getGithubMember();

        when(oAuth2RequestService.getUserInfoByProvider(eq("token"), eq("facebook"), any()))
                .thenReturn(githubMember);
        when(socialService.login(githubMember)).thenReturn(githubMember);
        when(jwtHelper.createToken(any(), any())).thenReturn("token");

        ResultActions actions = mockMvc.perform(post("/social/login/facebook")
                .param("accessToken", "token")
                .contentType(MediaType.APPLICATION_JSON)).andDo(print());

        actions
                .andExpect(status().isOk());
    }

    @DisplayName("google으로 로그인")
    @Test
    public void login_by_google() throws Exception {
        Member githubMember = MemberFactory.getGithubMember();

        when(oAuth2RequestService.getUserInfoByProvider(eq("token"), eq("google"), any()))
                .thenReturn(githubMember);
        when(socialService.login(githubMember)).thenReturn(githubMember);
        when(jwtHelper.createToken(any(), any())).thenReturn("token");

        ResultActions actions = mockMvc.perform(post("/social/login/google")
                .param("accessToken", "token")
                .contentType(MediaType.APPLICATION_JSON)).andDo(print());

        actions
                .andExpect(status().isOk());
    }

    @DisplayName("지원하지 않는 oauth2 로그인 시 UnSupportedException 발생")
    @Test
    public void login_by_no_provider() throws Exception {
        when(oAuth2RequestService.getUserInfoByProvider(eq("token"), eq("wrong"), any()))
                .thenThrow(UnSupportedOAuth2Exception.class);

        ResultActions actions = mockMvc.perform(post("/social/login/wrong")
                .param("accessToken", "token")
                .contentType(MediaType.APPLICATION_JSON)).andDo(print());

        actions
                .andExpect(status().isBadRequest());
    }

}