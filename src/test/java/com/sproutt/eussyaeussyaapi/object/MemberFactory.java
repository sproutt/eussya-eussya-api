package com.sproutt.eussyaeussyaapi.object;

import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.Provider;
import com.sproutt.eussyaeussyaapi.utils.RandomGenerator;

public class MemberFactory {

    private static final String DEFAULT_MEMBER_ID = "kjkun7631@naver.com";
    private static final String DEFAULT_PASSWORD = "12345aA!";
    private static final String DEFAULT_NAME = "test";

    public static Member getDefaultMember() {
        return Member.builder()
                     .memberId(DEFAULT_MEMBER_ID)
                     .password(DEFAULT_PASSWORD)
                     .nickName(DEFAULT_NAME)
                     .provider(Provider.LOCAL)
                     .authentication(RandomGenerator.createAuthenticationCode())
                     .build();
    }

    public static Member getGithubMember() {
        return Member.builder()
                     .memberId("41421173")
                     .nickName("Byeongjae Jung")
                     .provider(Provider.GITHUB)
                     .build();
    }

    public static Member getGoogleMember() {
        return Member.builder()
                     .memberId("105393180124787452232")
                     .nickName("Byeongjae Jung")
                     .provider(Provider.GOOGLE)
                     .email("jbj616@gmail.com")
                     .build();
    }

    public static Member getFacebookMember() {
        return Member.builder()
                     .memberId("2989263527824872")
                     .nickName("정병재")
                     .email("jbj616@naver.com")
                     .build();
    }
}