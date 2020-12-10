package com.sproutt.eussyaeussyaapi.domain.mission.exceptions;

import com.sproutt.eussyaeussyaapi.api.exception.message.MissionExceptionMessage;

public class LimitedTimeExceedException extends RuntimeException {
    public LimitedTimeExceedException() {
        super(MissionExceptionMessage.LIMITED_TIME_EXCEED);
    }
}
