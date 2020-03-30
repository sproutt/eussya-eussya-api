package com.sproutt.eussyaeussyaapi.api.oauth2.service;

import com.sproutt.eussyaeussyaapi.api.oauth2.dto.FacebookOAuth2UserDto;
import com.sproutt.eussyaeussyaapi.api.oauth2.dto.GoogleOAuth2UserDto;
import com.sproutt.eussyaeussyaapi.api.oauth2.exception.OAuth2CommunicationException;
import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.MemberRepository;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.DuplicationMemberException;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.NoSuchMemberException;
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

    @Override
    public Member login(String accessToken){
        FacebookOAuth2UserDto facebookOAuth2UserDto = getFacebookUserInfo(accessToken);

        if (!memberRepository.existsByMemberId(facebookOAuth2UserDto.getId())) {
            return memberRepository.save(facebookOAuth2UserDto.toEntity());
        }

        return memberRepository.findByMemberId(facebookOAuth2UserDto.getId()).orElseThrow(NoSuchMemberException::new);
    }

    @Override
    public String getProvider() {
        return "facebook";
    }

    private FacebookOAuth2UserDto getFacebookUserInfo(String accessToken) {

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
}
