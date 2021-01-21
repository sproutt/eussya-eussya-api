package com.sproutt.eussyaeussyaapi.domain.member.exceptions;

public class UnauthenticatedEmailException extends RuntimeException {

    private static final String UN_AUTHENTICATED_EMAIL = "인증되지 않은 email입니다.";

    public UnauthenticatedEmailException() {
        super(UN_AUTHENTICATED_EMAIL);
    }

}
