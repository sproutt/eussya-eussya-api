package com.sproutt.eussyaeussyaapi.api.member.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class NickNameUpdateDTO {

    @Size(min = 2, max = 10)
    private String nickName;

    @Builder
    public NickNameUpdateDTO( String nickName) {
        this.nickName = nickName;
    }
}
