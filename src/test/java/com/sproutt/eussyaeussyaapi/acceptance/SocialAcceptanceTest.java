package com.sproutt.eussyaeussyaapi.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import com.sproutt.eussyaeussyaapi.object.EncryptedResourceGenerator;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Test
    public void login_with_existed_id_by_github() {
        memberRepository.save(MemberFactory.getGithubMember());

        ResponseEntity response = template
                .postForEntity("/social/login/github", getHeader(githubToken), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void login_with_no_existed_id_by_github() {
        ResponseEntity response = template
                .postForEntity("/social/login/github", getHeader(githubToken), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void login_with_wrong_token_by_github() {
        String wrongAccessToken = "wrongwrong";
        ResponseEntity response = template
                .postForEntity("/social/login/github", getHeader(wrongAccessToken), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    private HttpHeaders getHeader(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("accessToken", accessToken);

        return headers;
    }
}
