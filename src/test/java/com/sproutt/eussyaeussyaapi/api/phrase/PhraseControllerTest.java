package com.sproutt.eussyaeussyaapi.api.phrase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sproutt.eussyaeussyaapi.api.HeaderSetUpWithToken;
import com.sproutt.eussyaeussyaapi.api.phrase.dto.PhraseResponseDTO;
import com.sproutt.eussyaeussyaapi.application.phrase.PhraseService;
import com.sproutt.eussyaeussyaapi.domain.phrase.Phrase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PhraseController.class, excludeFilters = @ComponentScan.Filter(EnableWebSecurity.class))
@WithMockUser("fake_user")
public class PhraseControllerTest extends HeaderSetUpWithToken {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PhraseService phraseService;

    private HttpHeaders headers;

    @BeforeEach
    void setUp() throws Exception {
        headers = setUpHeader();
    }

    @Test
    @DisplayName("동기부여 글귀 요청 테스트")
    void requestPhraseTest() throws Exception {
        PhraseResponseDTO phrase = new Phrase(1l, "this is test text").toResponseDTO();

        given(phraseService.getRandomPhrase()).willReturn(phrase);


        ResultActions actions = mvc.perform(get("/phrase")
                .with(csrf())
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON))
                                   .andDo(print());

        actions.andExpect(status().isOk());
    }

    @Test
    @DisplayName("동기부여 글귀 추가 api 테스트")
    void createPhraseTest() throws Exception {
        String phraseText = "this is test text";
        Phrase phrase = new Phrase(phraseText);

        given(phraseService.create(phraseText)).willReturn(phrase);

        ResultActions actions = mvc.perform(post("/phrase")
                .with(csrf())
                .headers(headers)
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(phraseText)))
                                   .andDo(print());

        actions.andExpect(status().isCreated());
    }

    private static String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}