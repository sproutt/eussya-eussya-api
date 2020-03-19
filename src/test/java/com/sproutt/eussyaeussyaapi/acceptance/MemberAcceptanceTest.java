package com.sproutt.eussyaeussyaapi.acceptance;

import com.sproutt.eussyaeussyaapi.api.dto.JoinDTO;
import com.sproutt.eussyaeussyaapi.domain.Member;
import com.sproutt.eussyaeussyaapi.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
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
public class MemberAcceptanceTest {
    private static final String DEFAULT_MEMBER_ID = "test@gmail.com";
    private static final String DEFAULT_PASSWORD = "1111";
    private static final String DEFAULT_NAME = "test";

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
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
        memberRepository.save(defaultMember());

        JoinDTO wrongJoinDTO = JoinDTO.builder()
                .memberId(DEFAULT_MEMBER_ID)
                .nickName("test2")
                .password(DEFAULT_PASSWORD)
                .build();

        ResponseEntity response = template.postForEntity("/members", wrongJoinDTO, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private JoinDTO defaultSignUpDTO() {
        return JoinDTO.builder()
                .memberId(DEFAULT_MEMBER_ID)
                .password(DEFAULT_PASSWORD)
                .nickName(DEFAULT_NAME)
                .build();
    }

    private Member defaultMember() {
        return Member.builder()
                .memberId(DEFAULT_MEMBER_ID)
                .password(DEFAULT_PASSWORD)
                .nickName(DEFAULT_NAME)
                .build();
    }
}