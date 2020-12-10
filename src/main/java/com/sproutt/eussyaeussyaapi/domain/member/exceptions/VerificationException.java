package com.sproutt.eussyaeussyaapi.domain.member.exceptions;

import com.sproutt.eussyaeussyaapi.api.exception.message.MemberExceptionMessage;

public class VerificationException extends RuntimeException {
    public VerificationException() {
        super(MemberExceptionMessage.FAILED_VERIFICATION);
    }
}
