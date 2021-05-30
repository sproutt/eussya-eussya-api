package com.sproutt.eussyaeussyaapi.api.security.exception;

public class InvalidAccessTokenException extends RuntimeException {

    private static final String INVALID_ACCESS_TOKEN = "유효하지 않은 Access 토큰";
    public InvalidAccessTokenException() {
        super(INVALID_ACCESS_TOKEN);
    }
}
