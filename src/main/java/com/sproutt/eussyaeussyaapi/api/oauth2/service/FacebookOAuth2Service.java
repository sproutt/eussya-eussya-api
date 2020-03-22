package com.sproutt.eussyaeussyaapi.api.oauth2.service;

import com.sproutt.eussyaeussyaapi.api.oauth2.dto.FacebookOAuth2UserDto;
import com.sproutt.eussyaeussyaapi.api.oauth2.exception.OAuth2CommunicationException;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.DuplicationMemberException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
public class FacebookOAuth2Service implements OAuth2Service {

    private final RestTemplate restTemplate;
    private final MemberRepository memberRepository;

    @Value("${social.facebook.url}")
    private String requestUrl;

    private FacebookOAuth2UserDto getGithubUserInfo(String accessToken) {

        StringBuilder request = new StringBuilder();
        request.append(requestUrl)
            .append("?fields=id,name")
            .append("&access_token")
            .append(accessToken);

        try {
            ResponseEntity<FacebookOAuth2UserDto> response = restTemplate
                .getForEntity(request.toString(), FacebookOAuth2UserDto.class);

            return response.getBody();
        } catch (RestClientException e) {
            throw new OAuth2CommunicationException();
        }
    }

    @Override
    public Member getMemberInfo(String accessToken) {
        FacebookOAuth2UserDto FacebookOAuth2UserDto = getGithubUserInfo(accessToken);

        return memberRepository.findByMemberId(FacebookOAuth2UserDto.getId()).orElseThrow(RuntimeException::new);
    }

    @Override
    public Member createMember(String accessToken) {
        FacebookOAuth2UserDto facebookOAuth2UserDto = getGithubUserInfo(accessToken);

        Member member = memberRepository.findByMemberId(facebookOAuth2UserDto.getId()).orElse(null);

        if (member == null) {
            throw new DuplicationMemberException();
        }

        return memberRepository.save(facebookOAuth2UserDto.toEntity());
    }

    @Override
    public String getProvider() {
        return "facebook";
    }
}
