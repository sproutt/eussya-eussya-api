package com.sproutt.eussyaeussyaapi.api.member.dto;

import com.sproutt.eussyaeussyaapi.domain.member.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberTokenCommand {

    private Long id;
    private String memberId;
    private String nickName;
    private Role role;

    @Builder
    public MemberTokenCommand(Long id, String memberId, String nickName, Role role) {
        this.id = id;
        this.memberId = memberId;
        this.nickName = nickName;
        this.role = role;
    }
}