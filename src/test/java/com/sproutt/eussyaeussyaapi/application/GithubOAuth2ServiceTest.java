package com.sproutt.eussyaeussyaapi.application;

import com.sproutt.eussyaeussyaapi.api.oauth2.service.GithubOAuth2Service;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import com.sproutt.eussyaeussyaapi.domain.member.Provider;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.DuplicationMemberException;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.NoSuchMemberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "classpath:/application.yml")
public class GithubOAuth2ServiceTest {

    @Value("${social.github.token}")
    private String GITHUB_ACCESS_TOKEN;

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
    public void create_member_with_Github() {
        Member member = githubOAuth2Service.createMember(GITHUB_ACCESS_TOKEN);
        Member createdMember = memberRepository.findAll().get(0);

        assertThat(createdMember.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(createdMember.getNickName()).isEqualTo(member.getNickName());
    }

    @Test
    public void create_member_with_Github_이미_가입된() {
        Member githubMember = githubMember();
        memberRepository.save(githubMember);

        assertThrows(DuplicationMemberException.class, () -> githubOAuth2Service.createMember(GITHUB_ACCESS_TOKEN));
    }

    @Test
    public void getMemberInfo() {
        Member githubMember = githubMember();
        memberRepository.save(githubMember);

        Member savedMember = githubOAuth2Service.getMemberInfo(GITHUB_ACCESS_TOKEN);

        assertThat(savedMember.getMemberId()).isEqualTo(githubMember.getMemberId());
        assertThat(savedMember.getNickName()).isEqualTo(githubMember.getNickName());
    }

    @Test
    public void getMemberInfo_저장되지않은() {
        assertThrows(NoSuchMemberException.class, () -> githubOAuth2Service.getMemberInfo(GITHUB_ACCESS_TOKEN));
    }

    private Member githubMember() {
        return Member.builder()
                     .memberId("41421173")
                     .nickName("Byeongjae Jung")
                     .provider(Provider.GITHUB)
                     .build();
    }

}
