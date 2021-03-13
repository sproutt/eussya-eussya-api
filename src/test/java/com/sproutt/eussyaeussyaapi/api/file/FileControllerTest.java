package com.sproutt.eussyaeussyaapi.api.file;

import com.google.gson.Gson;
import com.sproutt.eussyaeussyaapi.api.HeaderSetUpWithToken;
import com.sproutt.eussyaeussyaapi.api.file.dto.FileDto;
import com.sproutt.eussyaeussyaapi.application.file.FileService;
import com.sproutt.eussyaeussyaapi.application.file.S3Service;
import com.sproutt.eussyaeussyaapi.application.member.MemberService;
import com.sproutt.eussyaeussyaapi.domain.file.File;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FileController.class, excludeFilters = @ComponentScan.Filter(EnableWebSecurity.class))
@EnableAspectJAutoProxy
@WithMockUser("fake_user")
public class FileControllerTest extends HeaderSetUpWithToken {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MemberService memberService;

    @MockBean
    private FileService fileService;

    @MockBean
    private S3Service s3Service;

    private Member loginMember;
    private HttpHeaders headers;

    @BeforeEach
    void setUp() throws Exception {
        headers = setUpHeader();
        loginMember = MemberFactory.getDefaultMember();
    }

    @Test
    @DisplayName("필 업로드 요청")
    void uploadProfileTest() throws Exception {
        //given
        MockMultipartFile file = new MockMultipartFile
                ("file", "test.jpg", "multipart/form-data", "Spring Framework".getBytes());
        SimpleDateFormat date = new SimpleDateFormat("yyyymmddHHmmss");
        given(memberService.findTokenOwner(any())).willReturn(loginMember);
        given(s3Service.getContentType(any())).willReturn("jpg");
        given(s3Service.upload(loginMember.getNickName(), file, "jpg"))
                .willReturn(loginMember.getNickName() + "_profile.jpg" + date.format(new Date()));

        //when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.multipart("/profile")
                .file(file)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf())
                .headers(headers));

        //then
        resultActions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("프로필 URL 요청 - 프로필 등록하지 않았던 경우")
    void getDefaultProfileTest() throws Exception {
        //given
        String tmpNickName = "kiki";
        String defaultStoragePath = "https://dugjnp7kky4tj.cloudfront.net/default_profile.jpg";
        FileDto fileDto = new FileDto(tmpNickName, defaultStoragePath);
        File file = null;
        given(fileService.findByNickName(tmpNickName)).willReturn(java.util.Optional.ofNullable(file));
        //when
        ResultActions resultActions = mockMvc.perform(get("/profile/" + tmpNickName)
                .with(csrf())
                .headers(headers)
                .characterEncoding("utf-8"))
                .andDo(print());
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().string(new Gson().toJson(fileDto)));
    }

    @Test
    @DisplayName("프로필 URL 요청 - 프로필 등록한 경우")
    void getProfileTest() throws Exception {
        //given
        Member member = MemberFactory.getDefaultMember();
        String storagePath = "https://dugjnp7kky4tj.cloudfront.net/test_profile.jpg";
        String fileKey = "kiki_profile.jpg";
        File file = new File(member, storagePath, fileKey);
        FileDto fileDto = new FileDto(member.getNickName(), storagePath);

        given(fileService.findByNickName(any())).willReturn(Optional.ofNullable(file));
        //when
        ResultActions resultActions = mockMvc.perform(get("/profile/" + member.getNickName())
                .with(csrf())
                .headers(headers)
                .characterEncoding("utf-8"))
                .andDo(print());
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().string(new Gson().toJson(fileDto)));
    }

}

