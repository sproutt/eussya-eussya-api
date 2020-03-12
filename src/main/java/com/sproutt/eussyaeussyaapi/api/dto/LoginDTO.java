package com.sproutt.eussyaeussyaapi.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {
    private String memberId;
    private String password;

    @Builder
    public LoginDTO(String memberId, String password) {
        this.memberId = memberId;
        this.password = password;
    }
}
