package com.sproutt.eussyaeussyaapi.domain.member.exceptions;

import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;

public class VerificationException extends RuntimeException {
    public VerificationException() {
        super(ExceptionMessage.FAILED_VERIFICATION);
    }
}
