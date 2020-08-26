package com.sproutt.eussyaeussyaapi.domain.mission.exceptions;

import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;

public class NotCompletedMissionException extends RuntimeException {
    public NotCompletedMissionException() {
        super(ExceptionMessage.NOT_COMPLETED_MISSION);
    }
}
