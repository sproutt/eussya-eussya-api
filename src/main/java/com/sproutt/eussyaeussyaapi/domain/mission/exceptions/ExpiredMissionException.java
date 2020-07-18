package com.sproutt.eussyaeussyaapi.domain.mission.exceptions;

import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;

public class ExpiredMissionException extends RuntimeException {
    public ExpiredMissionException() {
        super(ExceptionMessage.EXPIRED_MISSION);
    }
}
