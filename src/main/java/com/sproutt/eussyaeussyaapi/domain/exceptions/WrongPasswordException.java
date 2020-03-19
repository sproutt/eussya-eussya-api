package com.sproutt.eussyaeussyaapi.domain.exceptions;

import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;

public class WrongPasswordException extends RuntimeException {
    public WrongPasswordException() {
        super(ExceptionMessage.WRONG_PASSWORD);
    }
}
