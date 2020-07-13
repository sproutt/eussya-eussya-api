package com.sproutt.eussyaeussyaapi.api.mission.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class MissionDTO {

    @NotBlank
    @Size(max = 80)
    private String title;

    @NotBlank
    private String contents;

    @NotBlank
    private String deadlineTime;

    @Builder
    public MissionDTO(String title, String contents, String deadlineTime) {
        this.title = title;
        this.contents = contents;
        this.deadlineTime = deadlineTime;
    }
}
