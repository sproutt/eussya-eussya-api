package com.sproutt.eussyaeussyaapi.api.oauth2.dto;

import com.sproutt.eussyaeussyaapi.domain.member.Member;
import com.sproutt.eussyaeussyaapi.domain.member.Provider;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GithubOAuth2UserDTO extends OAuth2UserInfoDTO {

    public GithubOAuth2UserDTO() {
        this.setProvider(Provider.GITHUB);
    }

    public Member toEntity() {
        return Member.builder()
                     .memberId(this.getId())
                     .nickName(this.getName())
                     .provider(this.getProvider())
                     .build();
    }

}
