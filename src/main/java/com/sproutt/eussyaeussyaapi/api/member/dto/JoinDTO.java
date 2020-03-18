package com.sproutt.eussyaeussyaapi.api.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JoinDTO {

    private String memberId;
    private String name;
    private String password;

    @Builder
    public JoinDTO(String memberId, String name, String password) {
        this.memberId = memberId;
        this.name = name;
        this.password = password;
    }
}
