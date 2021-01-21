package com.sproutt.eussyaeussyaapi.domain.member.exceptions;

public class VerificationException extends RuntimeException {

    private static final String FAILED_VERIFICATION = "인증 실패";

    public VerificationException() {
        super(FAILED_VERIFICATION);
    }
}
