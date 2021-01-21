package com.sproutt.eussyaeussyaapi.api.security.exception;

public class InvalidRefreshTokenException extends RuntimeException {

    public static final String INVALID_REFRESH_TOKEN = "유효하지 않은 Refresh 토큰";

    public InvalidRefreshTokenException() {
        super(INVALID_REFRESH_TOKEN);
    }
}
