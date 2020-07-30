package com.sproutt.eussyaeussyaapi.api.mission.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberDTO {

    private Long id;
    private String memberId;
    private String nickName;

    @Builder
    public MemberDTO(Long id, String memberId, String nickName) {
        this.id = id;
        this.memberId = memberId;
        this.nickName = nickName;
    }
}
