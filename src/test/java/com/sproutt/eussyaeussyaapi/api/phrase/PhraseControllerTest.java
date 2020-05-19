package com.sproutt.eussyaeussyaapi.api.phrase;

import com.sproutt.eussyaeussyaapi.api.phrase.dto.PhraseResponseDTO;
import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.api.security.JwtInterceptor;
import com.sproutt.eussyaeussyaapi.application.phrase.PhraseService;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.phrase.Phrase;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PhraseController.class)
public class PhraseControllerTest {

    @Value("${jwt.header}")
    private String tokenKey;

    @Value("${jwt.secret}")
    private String secretKey;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PhraseService phraseService;

    @MockBean
    private JwtHelper jwtHelper;

    @MockBean
    private JwtInterceptor jwtInterceptor;

    private HttpHeaders headers;

    @BeforeEach
    void setUp() throws Exception {
        Member loginMember = MemberFactory.getDefaultMember();
        when(jwtHelper.createToken(any(), any())).thenReturn("token");
        String token = jwtHelper.createToken(secretKey, loginMember.toJwtInfo());

        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set(tokenKey, token);

        when(jwtInterceptor.preHandle(any(), any(), any())).thenReturn(true);
    }

    @Test
    @DisplayName("동기부여 글귀 요청 테스트")
    void requestPhraseTest() throws Exception {
        PhraseResponseDTO phrase = new Phrase(1l, "this is test text").toResponseDTO();

        given(phraseService.getRandomPhrase()).willReturn(phrase);


        ResultActions actions = mvc.perform(get("/phrase")
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                                   .andDo(print());

        actions.andExpect(status().isOk());
    }
}
