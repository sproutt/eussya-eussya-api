package com.sproutt.eussyaeussyaapi.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class EmailDTO {

    @NotNull
    @Email
    private String email;
}
