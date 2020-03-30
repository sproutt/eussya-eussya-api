package com.sproutt.eussyaeussyaapi.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sproutt.eussyaeussyaapi.api.oauth2.exception.NotFoundOAuth2Exception;
import com.sproutt.eussyaeussyaapi.api.oauth2.exception.OAuth2CommunicationException;
import com.sproutt.eussyaeussyaapi.api.oauth2.service.GithubOAuth2Service;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.DuplicationMemberException;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.NoSuchMemberException;
import com.sproutt.eussyaeussyaapi.object.EncryptedResourceGenerator;
import com.sproutt.eussyaeussyaapi.object.MemberFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSendException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GithubOAuth2ServiceTest {

    private String githubToken = EncryptedResourceGenerator.getGitToken();

    @Autowired
    private GithubOAuth2Service githubOAuth2Service;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    public void before() {
        memberRepository.deleteAll();
        memberRepository.flush();
    }

    @Test
    public void login_with_no_existed_id_by_github() {
        Member member = githubOAuth2Service.login(githubToken);
        Member createdMember = memberRepository.findAll().get(0);

        assertThat(createdMember.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(createdMember.getNickName()).isEqualTo(member.getNickName());
    }

    @Test
    public void login_with_existed_id_by_github() {
        Member githubMember = MemberFactory.getGithubMember();
        memberRepository.save(githubMember);

        Member existedMember = githubOAuth2Service.login(githubToken);
        assertThat(existedMember.getMemberId()).isEqualTo(githubMember.getMemberId());
        assertThat(existedMember.getNickName()).isEqualTo(githubMember.getNickName());
    }

    @Test
    public void login_with_wrong_token_by_github() {
        String wrongAccessToken = "wrongwrong";

        assertThrows(OAuth2CommunicationException.class, () -> githubOAuth2Service.login(wrongAccessToken));
    }


}
