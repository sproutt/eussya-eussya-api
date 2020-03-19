package com.sproutt.eussyaeussyaapi.domain.member.exceptions;

import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super(ExceptionMessage.WRONG_PASSWORD);
    }
}
