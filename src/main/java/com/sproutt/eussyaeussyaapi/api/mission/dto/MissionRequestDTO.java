package com.sproutt.eussyaeussyaapi.api.mission.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MissionRequestDTO {

    @NotBlank
    @Size(max = 80)
    private String title;

    @NotBlank
    private String contents;

    @NotNull
    private LocalDateTime deadlineTime;

    @Builder
    public MissionRequestDTO(String title, String contents, LocalDateTime deadlineTime) {
        this.title = title;
        this.contents = contents;
        this.deadlineTime = deadlineTime;
    }
}
