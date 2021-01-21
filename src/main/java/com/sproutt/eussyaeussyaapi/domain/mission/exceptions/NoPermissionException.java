package com.sproutt.eussyaeussyaapi.domain.mission.exceptions;

public class NoPermissionException extends RuntimeException {

    private static final String NO_PERMISSION = "권한 없음";

    public NoPermissionException() {
        super(NO_PERMISSION);
    }
}
