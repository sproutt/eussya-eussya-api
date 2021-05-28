package com.sproutt.eussyaeussyaapi.api.member;

import com.sproutt.eussyaeussyaapi.api.HeaderSetUpWithToken;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.application.profile.ProfileService;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProfileController.class, excludeFilters = @ComponentScan.Filter(EnableWebSecurity.class))
@EnableAspectJAutoProxy
@WithMockUser("fake_user")
public class ProfileControllerTest extends HeaderSetUpWithToken {

    private Member loginMember;
    private HttpHeaders headers;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private ProfileService profileService;

    @Value("${cloud.aws.cloudFront.domain}")
    private String cloudFrontDomain;

    @BeforeEach
    void setUp() throws Exception {
        headers = setUpHeader();
        loginMember = MemberFactory.getDefaultMember();
    }

    @Test
    @DisplayName("프로필 업로드를 요청할 경우 Member객체의 profilePath에 URL이 저장된다.")
    void uploadProfileTest() throws Exception {
        //given
        MockMultipartFile file = new MockMultipartFile
                ("file", "test.jpg", "multipart/form-data", "Spring Framework".getBytes());
        String postFix = "-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        given(memberService.findTokenOwner(any())).willReturn(loginMember);
        given(profileService.isImageType(file.getOriginalFilename())).willReturn(true);
        loginMember.saveProfilePath(cloudFrontDomain + loginMember.getNickName() + postFix);
        given(profileService.uploadProfile(any(), any())).willReturn(loginMember.getProfilePath());
        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.multipart("/members/{id}/profile", 99)
                .file(file)
                .with(csrf())
                .headers(headers));
        //then
        resultActions.andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("기본 프로필로 수정을 요청할 경우 사용자의 프로필이 기본 프로필 경로로 바뀐다.")
    void resetProfileTest() throws Exception {
        //given
        given(memberService.findTokenOwner(any())).willReturn(loginMember);
        //when
        ResultActions resultActions = mockMvc.perform(put("/members/{id}/profile", 99)
                .with(csrf())
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
        //then
        resultActions.andExpect(status().isForbidden());
    }
}


