package com.sproutt.eussyaeussyaapi.api.member.dto;

import com.sproutt.eussyaeussyaapi.utils.RegexExpression;
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
public class PasswordUpdateDTO {

    @Nullable
    @Pattern(regexp = RegexExpression.PASSWORD)
    private String password;

    @Builder
    public PasswordUpdateDTO(String password) {
        this.password = password;
    }
}
