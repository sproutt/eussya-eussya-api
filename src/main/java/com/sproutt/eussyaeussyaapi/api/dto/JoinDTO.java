package com.sproutt.eussyaeussyaapi.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
public class JoinDTO {

    @NotBlank
    private String memberId;

    @NotBlank
    private String nickName;

    @Nullable
    @Pattern(regexp = RegexExpression.PASSWORD)
    private String password;

    @Builder
    public JoinDTO(String memberId, String nickName, String password) {
        this.memberId = memberId;
        this.nickName = nickName;
        this.password = password;
    }
}
