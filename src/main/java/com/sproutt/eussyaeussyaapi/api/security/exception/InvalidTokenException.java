package com.sproutt.eussyaeussyaapi.api.security.exception;

import com.sproutt.eussyaeussyaapi.api.exception.message.SecurityExceptionMessage;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super(SecurityExceptionMessage.INVALID_TOKEN);
    }
}
