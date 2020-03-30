package com.sproutt.eussyaeussyaapi.acceptance;

import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import com.sproutt.eussyaeussyaapi.object.EncryptedResourceGenerator;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;
import org.junit.jupiter.api.BeforeEach;
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

import static org.assertj.core.api.Assertions.assertThat;

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
    public void create_member_by_github() {
        ResponseEntity response = template
            .postForEntity("/social/signup/github", getHeader(githubToken), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void create_member_by_github_but_already_exist_memberId() {
        memberRepository.save(MemberFactory.getGithubMember());

        ResponseEntity response = template
            .postForEntity("/social/signup/github", getHeader(githubToken), String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody().toString()).contains(ExceptionMessage.DUPLICATED_MEMBER_ID);
    }

    private HttpHeaders getHeader(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("accessToken", accessToken);

        return headers;
    }
}
