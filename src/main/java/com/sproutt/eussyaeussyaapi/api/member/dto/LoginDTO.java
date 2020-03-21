package com.sproutt.eussyaeussyaapi.api.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginDTO {

    @Email
    @NotBlank
    private String memberId;

    @NotBlank
    private String password;

    @Builder
    public LoginDTO(String memberId, String password) {
        this.memberId = memberId;
        this.password = password;
    }
}
