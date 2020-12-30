package com.sproutt.eussyaeussyaapi.api.oauth2.dto;

import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.Provider;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleOAuth2UserDTO extends OAuth2UserInfoDTO {

    private String email;

    public GoogleOAuth2UserDTO() {
        this.setProvider(Provider.GOOGLE);
    }

    @Override
    public Member toEntity() {
        return Member.builder()
                     .memberId(this.getId())
                     .nickName(this.getName())
                     .email(email)
                     .provider(this.getProvider())
                     .build();
    }
}