package com.sproutt.eussyaeussyaapi.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import com.sproutt.eussyaeussyaapi.object.EncryptedResourceGenerator;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class SocialAcceptanceTest {

    private String githubToken = EncryptedResourceGenerator.getGitToken();

    @Autowired
    private TestRestTemplate template;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void init() {
        memberRepository.deleteAll();
        memberRepository.flush();
    }

    @DisplayName("깃헙으로 로그인하기 - DB에 저장되어있는 User일 때 OK(200)")
    @Test
    public void login_with_existed_id_by_github() {
        memberRepository.save(MemberFactory.getGithubMember());

        ResponseEntity response = template
            .postForEntity("/social/login/github", getHeader(githubToken), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @DisplayName("깃헙으로 로그인하기 - 처음 로그인할 때 OK(200)")
    @Test
    public void login_with_no_existed_id_by_github() {
        ResponseEntity response = template
            .postForEntity("/social/login/github", getHeader(githubToken), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @DisplayName("깃헙으로 로그인하기 - accessToken이 잘못되었을 때 Bad_Request(400)")
    @Test
    public void login_with_wrong_token_by_github() {
        String wrongAccessToken = "wrongwrong";
        ResponseEntity response = template
            .postForEntity("/social/login/github", getHeader(wrongAccessToken), String.class);

        assertThat(response.getBody()).isEqualTo("oauth 통신 중 오류가 발생하였습니다.");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @DisplayName("지원하지 않는 로그인 주소일 때 Bad_Request(400)")
    @Test
    public void wrong_provider() {

        ResponseEntity response = template
            .postForEntity("/social/login/wrong", getHeader("anything"), String.class);

        assertThat(response.getBody()).isEqualTo("지원하지 않거나 존재하지 않는 oauth 입니다.");
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private HttpHeaders getHeader(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("accessToken", accessToken);

        return headers;
    }
}
