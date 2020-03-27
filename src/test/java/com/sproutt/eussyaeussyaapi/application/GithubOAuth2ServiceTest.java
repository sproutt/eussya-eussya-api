package com.sproutt.eussyaeussyaapi.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    public void create_member_with_Github() {
        Member member = githubOAuth2Service.createMember(githubToken);
        Member createdMember = memberRepository.findAll().get(0);

        assertThat(createdMember.getMemberId()).isEqualTo(member.getMemberId());
        assertThat(createdMember.getNickName()).isEqualTo(member.getNickName());
    }

    @Test
    public void create_member_with_Github_이미_가입된() {
        Member githubMember = MemberFactory.getGithubMember();
        memberRepository.save(githubMember);

        assertThrows(DuplicationMemberException.class, () -> githubOAuth2Service.createMember(githubToken));
    }

    @Test
    public void getMemberInfo() {
        Member githubMember = MemberFactory.getGithubMember();
        memberRepository.save(githubMember);

        Member savedMember = githubOAuth2Service.getMemberInfo(githubToken);

        assertThat(savedMember.getMemberId()).isEqualTo(githubMember.getMemberId());
        assertThat(savedMember.getNickName()).isEqualTo(githubMember.getNickName());
    }

    @Test
    public void getMemberInfo_저장되지않은() {
        assertThrows(NoSuchMemberException.class, () -> githubOAuth2Service.getMemberInfo(githubToken));
    }
}
