package com.sproutt.eussyaeussyaapi.api;

import com.sproutt.eussyaeussyaapi.domain.MemberRepository;
import com.sproutt.eussyaeussyaapi.domain.dto.JoinDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberAcceptanceTest {
    private static final String DEFAULT_MEMBER_ID = "test@gmail.com";
    private static final String DEFAULT_PASSWORD = "1111";
    private static final String DEFAULT_NAME = "test";

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private MemberRepository memberRepository;

    @Before
    public void init() {
        memberRepository.deleteAll();
        memberRepository.flush();
    }

    @Test
    public void createMember() {
        JoinDTO joinDTO = defaultSignUpDTO();
        ResponseEntity response = template.postForEntity("/members", joinDTO, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void createMember_with_already_exist_memberId() {
        memberRepository.save(defaultSignUpDTO().toEntity());

        JoinDTO wrongJoinDTO = JoinDTO.builder()
                .memberId(DEFAULT_MEMBER_ID)
                .name("test2")
                .password(DEFAULT_PASSWORD)
                .build();

        ResponseEntity response = template.postForEntity("/members", wrongJoinDTO, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private JoinDTO defaultSignUpDTO() {
        return JoinDTO.builder()
                .memberId(DEFAULT_MEMBER_ID)
                .password(DEFAULT_PASSWORD)
                .name(DEFAULT_NAME)
                .build();
    }
}