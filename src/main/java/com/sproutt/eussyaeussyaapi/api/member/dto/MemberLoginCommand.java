package com.sproutt.eussyaeussyaapi.api.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class MemberLoginCommand {

    @Email
    @NotBlank
    private String memberId;

    @NotBlank
    private String password;

    @Builder
    public MemberLoginCommand(String memberId, String password) {
        this.memberId = memberId;
        this.password = password;
    }
}
