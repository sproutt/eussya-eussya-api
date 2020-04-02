package com.sproutt.eussyaeussyaapi.api.exception;

import com.sproutt.eussyaeussyaapi.api.member.dto.JoinDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GlobalExceptionHandlerTest {

    @Autowired
    private TestRestTemplate template;


    @Test
    @DisplayName("예외 메세지 핸들러 테스트")
    void validationMessageTest() {
        JoinDTO joinDTO = JoinDTO.builder()
                                 .memberId("")
                                 .password("")
                                 .nickName("")
                                 .build();

        ResponseEntity<String> response = template.postForEntity("/members", joinDTO, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.toString()).contains("비밀번호");
        assertThat(response.toString()).contains("이메일");
        assertThat(response.toString()).contains("닉네임");
    }
}
