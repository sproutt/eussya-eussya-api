package com.sproutt.eussyaeussyaapi.api.mission.dto;

import com.sproutt.eussyaeussyaapi.domain.mission.Mission;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CompleteMissionResponseDTO extends MissionResponseDTO {

    private String result;

    public CompleteMissionResponseDTO(Mission mission) {
        super(mission);
        this.result = mission.getResult();
    }
}
