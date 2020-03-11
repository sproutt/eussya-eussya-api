package com.sproutt.eussyaeussyaapi.domain.dto;

import com.sproutt.eussyaeussyaapi.domain.Member;
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

    public Member toEntity() {

        return Member.builder()
                .memberId(this.memberId)
                .name(this.name)
                .password(this.password)
                .build();
    }
}
