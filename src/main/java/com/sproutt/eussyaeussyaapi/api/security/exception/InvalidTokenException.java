package com.sproutt.eussyaeussyaapi.api.security.exception;

import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException() {
        super(ExceptionMessage.INVALID_TOKEN);
    }
}
