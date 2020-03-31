package com.sproutt.eussyaeussyaapi.api.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class EmailAuthDTO {

    @Email
    @NotBlank
    private String memberId;

    @NotBlank
    private String authCode;

    @Builder
    public EmailAuthDTO(@Email @NotBlank String memberId, @NotBlank String authCode) {
        this.memberId = memberId;
        this.authCode = authCode;
    }

    @Override
    public String toString() {
        return "EmailAuthDTO{" +
                "id='" + memberId + '\'' +
                ", authCode='" + authCode + '\'' +
                '}';
    }
}
