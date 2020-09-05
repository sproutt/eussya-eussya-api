package com.sproutt.eussyaeussyaapi.api.mission.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class CompleteMissionRequestDTO {

    @NotBlank
    String timeFormattedAsISO;

    String result;

    public CompleteMissionRequestDTO(String timeFormattedAsISO, String result) {
        this.timeFormattedAsISO = timeFormattedAsISO;
        this.result = result;
    }
}
