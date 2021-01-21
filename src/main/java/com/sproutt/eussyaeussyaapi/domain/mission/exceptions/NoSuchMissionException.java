package com.sproutt.eussyaeussyaapi.domain.mission.exceptions;

public class NoSuchMissionException extends RuntimeException {

    public static final String NO_SUCH_MISSION = "존재하지 않는 미션입니다.";

    public NoSuchMissionException() {
        super(NO_SUCH_MISSION);
    }
}
