package com.sproutt.eussyaeussyaapi.api.security.exception;

import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;

public class InvalidAccessTokenException extends RuntimeException {
    public InvalidAccessTokenException() {
        super(ExceptionMessage.INVALID_ACCESS_TOKEN);
    }
}
