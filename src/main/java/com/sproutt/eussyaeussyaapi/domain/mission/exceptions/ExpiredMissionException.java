package com.sproutt.eussyaeussyaapi.domain.mission.exceptions;

import com.sproutt.eussyaeussyaapi.api.exception.message.MissionExceptionMessage;

public class ExpiredMissionException extends RuntimeException {
    public ExpiredMissionException() {
        super(MissionExceptionMessage.EXPIRED_MISSION);
    }
}
