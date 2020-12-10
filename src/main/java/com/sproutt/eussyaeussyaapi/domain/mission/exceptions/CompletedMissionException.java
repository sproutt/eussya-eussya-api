package com.sproutt.eussyaeussyaapi.domain.mission.exceptions;

import com.sproutt.eussyaeussyaapi.api.exception.message.MissionExceptionMessage;

public class CompletedMissionException extends RuntimeException {
    public CompletedMissionException() {
        super(MissionExceptionMessage.COMPLETED_MISSION);
    }
}
