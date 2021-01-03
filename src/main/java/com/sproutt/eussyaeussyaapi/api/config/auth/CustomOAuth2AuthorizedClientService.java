package com.sproutt.eussyaeussyaapi.api.config.auth;

import com.sproutt.eussyaeussyaapi.api.config.auth.exception.UnSupportedOAuth2Exception;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import com.sproutt.eussyaeussyaapi.utils.exception.NotImplementedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuth2AuthorizedClientService implements OAuth2AuthorizedClientService {

    private final MemberRepository memberRepository;

    public CustomOAuth2AuthorizedClientService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public <T extends OAuth2AuthorizedClient> T loadAuthorizedClient(String clientRegistrationId, String principalName) {
        throw new NotImplementedException();
    }

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        String provider = authorizedClient.getClientRegistration().getRegistrationId();
        OAuth2User oauth2User = (OAuth2User) principal.getPrincipal();

        OAuth2UserInfo userInfo = getUserInfo(provider, oauth2User);
        Member member = memberRepository.findByMemberId(userInfo.getMemberId())
                                        .orElse(userInfo.toEntity());

        if (userInfo.getEmail() != null) {
            member.verifyEmail();
        }

        memberRepository.save(member);
    }

    private OAuth2UserInfo getUserInfo(String provider, OAuth2User oAuth2User) {
        if (provider.equals("github")) {
            return new GithubOAuth2UserInfo(oAuth2User.getAttributes());
        }

        throw new UnSupportedOAuth2Exception();
    }

    @Override
    public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
        throw new NotImplementedException();
    }
}
