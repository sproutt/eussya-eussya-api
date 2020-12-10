package com.sproutt.eussyaeussyaapi.domain.mission.exceptions;

import com.sproutt.eussyaeussyaapi.api.exception.message.MissionExceptionMessage;

public class NotCompletedMissionException extends RuntimeException {
    public NotCompletedMissionException() {
        super(MissionExceptionMessage.NOT_COMPLETED_MISSION);
    }
}
