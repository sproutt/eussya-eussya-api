package com.sproutt.eussyaeussyaapi.domain.mission.exceptions;

import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;

public class NoSuchMissionException extends RuntimeException {
    public NoSuchMissionException() {
        super(ExceptionMessage.NO_SUCH_MISSION);
    }
}
