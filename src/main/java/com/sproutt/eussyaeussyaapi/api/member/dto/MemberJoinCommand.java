package com.sproutt.eussyaeussyaapi.api.member.dto;

import com.sproutt.eussyaeussyaapi.utils.RegexExpression;
import io.swagger.annotations.ApiParam;
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
public class MemberJoinCommand {

    @NotBlank
    @ApiParam(value = "회원 ID", required = true)
    private String memberId;

    @Size(min = 2, max = 10)
    @ApiParam(value = "회원 닉네임", required = true)
    private String nickName;

    @Nullable
    @Pattern(regexp = RegexExpression.PASSWORD)
    @ApiParam(value = "회원 비밀번호", required = true)
    private String password;

    @Builder
    public MemberJoinCommand(String memberId, String nickName, String password) {
        this.memberId = memberId;
        this.nickName = nickName;
        this.password = password;
    }
}
