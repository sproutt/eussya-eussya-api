package com.sproutt.eussyaeussyaapi.api.member.dto;

import com.sproutt.eussyaeussyaapi.domain.member.Provider;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtMemberDTO {

    Long id;

    private String memberId;

    private String email;

    private String nickName;

    private Provider provider;

    @Builder
    public JwtMemberDTO(Long id, String memberId, String email, String nickName, Provider provider) {
        this.id = id;
        this.memberId = memberId;
        this.email = email;
        this.nickName = nickName;
        this.provider = provider;
    }
}
