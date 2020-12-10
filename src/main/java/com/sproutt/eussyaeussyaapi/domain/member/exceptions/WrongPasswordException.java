package com.sproutt.eussyaeussyaapi.domain.member.exceptions;

import com.sproutt.eussyaeussyaapi.api.exception.message.MemberExceptionMessage;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super(MemberExceptionMessage.WRONG_PASSWORD);
    }
}
