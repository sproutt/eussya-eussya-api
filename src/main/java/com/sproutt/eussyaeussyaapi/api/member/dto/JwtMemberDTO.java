package com.sproutt.eussyaeussyaapi.api.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtMemberDTO {

    Long id;

    private String nickName;

    @Builder
    public JwtMemberDTO(Long id, String nickName) {
        this.id = id;
        this.nickName = nickName;
    }
}
