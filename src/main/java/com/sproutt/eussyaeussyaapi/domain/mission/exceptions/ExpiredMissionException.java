package com.sproutt.eussyaeussyaapi.domain.mission.exceptions;

public class ExpiredMissionException extends RuntimeException {

    private static final String EXPIRED_MISSION = "종료된 미션입니다.";

    public ExpiredMissionException() {
        super(EXPIRED_MISSION);
    }
}
