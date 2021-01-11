package com.sproutt.eussyaeussyaapi.api.security.auth;

import com.sproutt.eussyaeussyaapi.api.security.auth.exception.UnSupportedOAuth2Exception;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    public CustomOAuth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId();
        OAuth2UserInfo userInfo = getUserInfo(provider, oAuth2User);

        Member member = memberRepository.findByMemberId(userInfo.getMemberId())
                                        .orElse(userInfo.toEntity());

        if (userInfo.getEmail() != null) {
            member.verifyEmail();
        }

        memberRepository.save(member);

        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint()
                                                  .getUserNameAttributeName();

        return new DefaultOAuth2User(oAuth2User.getAuthorities(), userInfo.getAttributes(), userNameAttributeName);
    }

    private OAuth2UserInfo getUserInfo(String provider, OAuth2User oAuth2User) {
        if (provider.equals("github")) {
            return new GithubOAuth2UserInfo(oAuth2User.getAttributes());
        }

        throw new UnSupportedOAuth2Exception();
    }
}
