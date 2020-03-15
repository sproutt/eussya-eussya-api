package com.sproutt.eussyaeussyaapi.application;

import com.sproutt.eussyaeussyaapi.api.dto.oauth2.OAuth2UserDto;
import com.sproutt.eussyaeussyaapi.api.dto.oauth2.OAuth2UserFactory;
import com.sproutt.eussyaeussyaapi.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserDto oAuth2UserDto = OAuth2UserFactory
            .getOAuth2UserDto(userRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());

        memberRepository.save(oAuth2UserDto.toEntity());

        return super.loadUser(userRequest);
    }
}
