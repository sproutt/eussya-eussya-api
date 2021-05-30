package com.sproutt.eussyaeussyaapi.api.mission.dto;

import com.sproutt.eussyaeussyaapi.domain.mission.Mission;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompletedMissionResponseDTO extends MissionResponseDTO {

    private String result;

    public CompletedMissionResponseDTO(Mission mission) {
        super(mission);
        this.result = mission.getResult();
    }
}
