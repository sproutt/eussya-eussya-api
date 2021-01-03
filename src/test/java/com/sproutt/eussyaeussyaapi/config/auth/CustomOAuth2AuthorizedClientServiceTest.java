package com.sproutt.eussyaeussyaapi.config.auth;

import com.sproutt.eussyaeussyaapi.api.config.auth.CustomOAuth2AuthorizedClientService;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DataJpaTest
public class CustomOAuth2AuthorizedClientServiceTest {

    private OAuth2AuthorizedClientService customOAuth2AuthorizedClientService;

    @Autowired
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        customOAuth2AuthorizedClientService = new CustomOAuth2AuthorizedClientService(memberRepository);
    }

    @Test
    @DisplayName("github 소셜 로그인 시 db에 저장되는지")
    void register_validAuthorizedClient_should_save_db() {
        assertNull(memberRepository.findByMemberId("github_mock_id").orElse(null));

        OAuth2AuthorizedClient oAuth2AuthorizedClient = new OAuth2AuthorizedClient(CommonOAuth2Provider.GITHUB.getBuilder("github").clientId("mock_client_id").build(), "mock_principle_name", mock(OAuth2AccessToken.class));
        Authentication authentication = mock(Authentication.class);
        OAuth2User oAuth2User = mock(OAuth2User.class);

        when(authentication.getPrincipal()).thenReturn(oAuth2User);
        when(oAuth2User.getAttributes()).thenReturn(mockAttributes());

        customOAuth2AuthorizedClientService.saveAuthorizedClient(oAuth2AuthorizedClient, authentication);

        assertNotNull(memberRepository.findByMemberId("github_mock_id").orElse(null));
    }

    private Map<String, Object> mockAttributes() {
        Map<String, Object> attributes = new HashMap<>();

        attributes.put("id", "mock_id");
        attributes.put("login", "mock_nickname");
        attributes.put("email", "mock@email.com");

        return attributes;
    }
}
