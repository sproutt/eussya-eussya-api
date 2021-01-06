package com.sproutt.eussyaeussyaapi.api.security.exception;

import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException() {
        super(ExceptionMessage.INVALID_REFRESH_TOKEN);
    }
}
