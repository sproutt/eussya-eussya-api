package com.sproutt.eussyaeussyaapi.api.mission.dto;

import com.sproutt.eussyaeussyaapi.domain.mission.Mission;
import com.sproutt.eussyaeussyaapi.domain.mission.MissionStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class MissionResponseDTO {

    private Long id;
    private String title;
    private String contents;
    private Long writerId;
    private LocalDateTime deadlineTime;
    private LocalDateTime startTime;
    private MissionStatus status;

    public MissionResponseDTO(Mission mission) {
        this.id = mission.getId();
        this.title = mission.getTitle();
        this.contents = mission.getContents();
        this.writerId = mission.getWriter().getId();
        this.deadlineTime = mission.getDeadlineTime();
        this.startTime = mission.getStartTime();
        this.status = mission.getStatus();
    }
}
