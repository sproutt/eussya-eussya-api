package com.sproutt.eussyaeussyaapi.api.security.auth;

import com.sproutt.eussyaeussyaapi.api.security.JwtHelper;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CustomOAuth2SuccessHandlerTest {

    private MemberRepository memberRepository;
    private JwtHelper jwtHelper;
    private AuthenticationSuccessHandler customOAuth2SuccessHandler;

    @BeforeEach
    void setUp() {
        memberRepository = mock(MemberRepository.class);
        jwtHelper = mock(JwtHelper.class);

        customOAuth2SuccessHandler = new CustomOAuth2SuccessHandler(jwtHelper, memberRepository);
    }

    @Test
    @DisplayName("onAuthenticationSuccess 메소드 테스트 - 정상적인 요청의 경우 redirect url의 query string으로 access token과 refresh token이 발급됨")
    public void responseIsForwarded() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        String id = "github_mockId";
        Map<String, Object> details = new HashMap<>();
        details.put("id", id);
        details.put("login", "mock_nickname");
        details.put("email", "mock@email.com");
        details.put("url", "https://api.github.com");

        Authentication authentication = new MockAuthentication(mock(ArrayList.class), details);

        Member mockMember = Member.builder()
                              .memberId("github_mockId")
                              .nickName("mockNickname")
                              .build();
        String token = "mock_token";

        when(memberRepository.findByMemberId(id)).thenReturn(Optional.ofNullable(mockMember));
        when(jwtHelper.createAccessToken(any())).thenReturn(token);
        when(jwtHelper.createRefreshToken(any())).thenReturn(token);

        customOAuth2SuccessHandler.onAuthenticationSuccess(request, response, authentication);
        assertThat(response.getRedirectedUrl().contains(token)).isTrue();
    }

    class MockAuthentication implements Authentication {
        private List<GrantedAuthority> authorities;
        private Map<String, Object> details;

        public MockAuthentication(List<GrantedAuthority> authorities, Map<String, Object> details) {
            this.authorities = authorities;
            this.details = details;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public Object getCredentials() {
            return null;
        }

        @Override
        public Object getDetails() {
            return details;
        }

        @Override
        public Object getPrincipal() {
            return null;
        }

        @Override
        public boolean isAuthenticated() {
            return false;
        }

        @Override
        public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

        }

        @Override
        public String getName() {
            return (String) details.get("id");
        }

        @Override
        public String toString() {
            return "MockAuthentication{" +
                    "authorities=" + authorities +
                    ", details={" + "url=" + details.get("url") +
                    '}';
        }
    }
}