package com.sproutt.eussyaeussyaapi.domain.mission.exceptions;

public class NotCompletedMissionException extends RuntimeException {

    private static final String NOT_COMPLETED_MISSION = "완료하지 않은 미션은 결과물을 작성할 수 없습니다.";

    public NotCompletedMissionException() {
        super(NOT_COMPLETED_MISSION);
    }
}
