package com.sproutt.eussyaeussyaapi.api.security.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GithubOAuth2UserInfoTest {

    private Map<String, Object> attributes;
    private static final String ID = "mock_id";
    private static final String NICKNAME = "mock_nickname";
    private static final String EMAIL = "mock_email";

    @BeforeEach
    void setUp() {
        attributes = new HashMap<>();

        attributes.put("id", ID);
        attributes.put("login", NICKNAME);
        attributes.put("email", EMAIL);

    }

    @Test
    @DisplayName("유효하지 않는 attributes를 파라미터로 갖는 GithubOAuth2UserInfo 생성자 인스턴스 테스트 - id가 비어있는 경우")
    void constructor_with_emptyId_throw_IllegalArgumentException() {
        attributes.put("id", null);
        assertThrows(IllegalArgumentException.class, () -> new GithubOAuth2UserInfo(this.attributes));

        attributes.put("id", "");
        assertThrows(IllegalArgumentException.class, () -> new GithubOAuth2UserInfo(this.attributes));

        attributes.remove("id");
        assertThrows(IllegalArgumentException.class, () -> new GithubOAuth2UserInfo(this.attributes));
    }

    @Test
    @DisplayName("유효하지 않는 attributes를 파라미터로 갖는 GithubOAuth2UserInfo 생성자 인스턴스 테스트 - id가 비어있는 경우")
    void constructor_with_emptyNickName_throw_IllegalArgumentException() {
        attributes.put("login", null);
        assertThrows(IllegalArgumentException.class, () -> new GithubOAuth2UserInfo(this.attributes));

        attributes.put("login", "");
        assertThrows(IllegalArgumentException.class, () -> new GithubOAuth2UserInfo(this.attributes));

        attributes.remove("login");
        assertThrows(IllegalArgumentException.class, () -> new GithubOAuth2UserInfo(this.attributes));
    }

    @Test
    @DisplayName("유효한 attributes를 파라미터로 갖는 GithubOAuth2UserInfo 생성자 인스턴스 테스트 - email이 비어있는 경우")
    void constructor_with_emptyEmail_created() {
        attributes.put("email", null);
        new GithubOAuth2UserInfo(this.attributes);

        attributes.put("email", "");
        new GithubOAuth2UserInfo(this.attributes);

        attributes.remove("email");
        new GithubOAuth2UserInfo(this.attributes);
    }

    @Test
    @DisplayName("유효한 attributes를 파라미터로 갖는 GithubOAuth2UserInfo 생성자 인스턴스 테스트")
    void constructor_with_validParameter_created() {
        OAuth2UserInfo oAuth2UserInfo = new GithubOAuth2UserInfo(this.attributes);

        assertEquals(oAuth2UserInfo.getMemberId(), "github_" + ID);
        assertEquals(oAuth2UserInfo.getEmail(), EMAIL);
        assertEquals(oAuth2UserInfo.getNickName(), NICKNAME);
    }
}
