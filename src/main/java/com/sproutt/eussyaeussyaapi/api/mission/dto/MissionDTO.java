package com.sproutt.eussyaeussyaapi.api.mission.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class MissionDTO {

    @NotNull
    @Size(max = 80)
    private String title;

    @NotNull
    private String contents;

    @NotNull
    private int goalHours;

    @Builder
    public MissionDTO(@NotNull @Size(max = 80) String title, @NotNull String contents, @NotNull int goalHours) {
        this.title = title;
        this.contents = contents;
        this.goalHours = goalHours;
    }
}
