package com.sproutt.eussyaeussyaapi.api.member.dto;

import com.sproutt.eussyaeussyaapi.utils.RegexExpression;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class JoinDTO {

    @NotBlank
    private String memberId;

    @Size(min = 2, max = 10)
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
