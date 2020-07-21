package com.sproutt.eussyaeussyaapi.api.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtMemberDTO {

    private Long id;
    private String memberId;
    private String nickName;

    @Builder
    public JwtMemberDTO(Long id, String memberId, String nickName) {
        this.id = id;
        this.memberId = memberId;
        this.nickName = nickName;
    }
}
