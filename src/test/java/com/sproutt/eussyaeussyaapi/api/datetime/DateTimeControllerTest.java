package com.sproutt.eussyaeussyaapi.api.datetime;

import com.sproutt.eussyaeussyaapi.api.HeaderSetUpWithToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(DateTimeController.class)
public class DateTimeControllerTest extends HeaderSetUpWithToken {

    @Autowired
    private MockMvc mvc;

    private HttpHeaders headers;

    @BeforeEach
    void setUp() throws Exception {
        headers = setUpHeader();
    }

    @Test
    @DisplayName("시간 api")
    void timeApi() throws Exception {
        ResultActions actions = mvc.perform(get("/time/now")
                .characterEncoding("utf-8")
                .headers(headers)
                .contentType(MediaType.APPLICATION_JSON))
                                   .andDo(print());

        actions.andExpect(status().isOk());
    }
}
