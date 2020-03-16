package com.sproutt.eussyaeussyaapi.application;

import com.sproutt.eussyaeussyaapi.api.dto.SessionMember;
import com.sproutt.eussyaeussyaapi.api.dto.oauth2.OAuth2AttributeDto;
import com.sproutt.eussyaeussyaapi.api.dto.oauth2.OAuth2AttributeFactory;
import com.sproutt.eussyaeussyaapi.domain.Member;
import com.sproutt.eussyaeussyaapi.domain.MemberRepository;
import java.util.Collections;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2AttributeDto oAuth2AttributeDto = OAuth2AttributeFactory.getOAuth2UserDto(registrationId, oAuth2User.getAttributes());

        saveOrUpdate(oAuth2AttributeDto);
        httpSession.setAttribute("user", new SessionMember(oAuth2AttributeDto));

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
            oAuth2User.getAttributes(),
            userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName());
    }

    private Member saveOrUpdate(OAuth2AttributeDto oAuth2AttributeDto) {

        Optional<Member> member = memberRepository.findByMemberId(oAuth2AttributeDto.getId());

        if (member.isPresent()) {
            Member updatedMember = member.get().update(oAuth2AttributeDto.toEntity());
            return memberRepository.save(updatedMember);
        }

        return memberRepository.save(oAuth2AttributeDto.toEntity());
    }
}
