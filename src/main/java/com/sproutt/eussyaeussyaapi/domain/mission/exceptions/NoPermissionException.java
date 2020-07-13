package com.sproutt.eussyaeussyaapi.domain.mission.exceptions;

import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;

public class NoPermissionException extends RuntimeException {
    public NoPermissionException() {
        super(ExceptionMessage.NO_PERMISSION);
    }
}
