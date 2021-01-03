package com.sproutt.eussyaeussyaapi.api.config.auth;

import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.Provider;
import com.sproutt.eussyaeussyaapi.utils.RandomGenerator;
import io.jsonwebtoken.lang.Assert;

import java.util.Map;

public class GithubOAuth2UserInfo implements OAuth2UserInfo {
    private Map<String, Object> attributes;

    public GithubOAuth2UserInfo(Map<String, Object> attributes) {
        Assert.hasText((String) attributes.get("id"), "id cannot be empty");
        Assert.hasText((String) attributes.get("login"), "nickname cannot be empty");

        this.attributes = attributes;
    }

    @Override
    public String getMemberId() {
        return "github_" + attributes.get("id");
    }

    @Override
    public String getNickName() {
        return (String) attributes.get("login");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public Provider getProvider() {
        return Provider.GITHUB;
    }

    public Member toEntity() {
        return Member.builder()
                     .memberId(this.getMemberId())
                     .nickName(this.getNickName())
                     .email(this.getEmail())
                     .provider(this.getProvider())
                     .password(RandomGenerator.createAuthenticationCode())
                     .build();
    }
}
