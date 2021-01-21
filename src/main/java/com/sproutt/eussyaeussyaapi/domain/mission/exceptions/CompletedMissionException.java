package com.sproutt.eussyaeussyaapi.domain.mission.exceptions;

public class CompletedMissionException extends RuntimeException {

    private static final String COMPLETED_MISSION = "이미 완료된 미션은 수정할 수 없습니다.";

    public CompletedMissionException() {
        super(COMPLETED_MISSION);
    }
}
