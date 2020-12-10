package com.sproutt.eussyaeussyaapi.domain.mission.exceptions;

import com.sproutt.eussyaeussyaapi.api.exception.message.MissionExceptionMessage;

public class NoPermissionException extends RuntimeException {
    public NoPermissionException() {
        super(MissionExceptionMessage.NO_PERMISSION);
    }
}
