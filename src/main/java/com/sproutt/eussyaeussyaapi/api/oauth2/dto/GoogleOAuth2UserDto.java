package com.sproutt.eussyaeussyaapi.api.oauth2.dto;

import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.Provider;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleOAuth2UserDto {

    private String id;

    private String name;

    private String email;

    public Member toEntity() {
        return Member.builder()
                     .memberId(id)
                     .nickName(name)
                     .provider(Provider.GOOGLE)
                     .build();
    }
}