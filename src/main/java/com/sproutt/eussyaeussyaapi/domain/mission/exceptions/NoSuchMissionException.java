package com.sproutt.eussyaeussyaapi.domain.mission.exceptions;

import com.sproutt.eussyaeussyaapi.api.exception.message.MissionExceptionMessage;

public class NoSuchMissionException extends RuntimeException {
    public NoSuchMissionException() {
        super(MissionExceptionMessage.NO_SUCH_MISSION);
    }
}
