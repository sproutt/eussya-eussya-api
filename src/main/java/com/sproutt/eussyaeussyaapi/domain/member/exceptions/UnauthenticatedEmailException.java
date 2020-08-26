package com.sproutt.eussyaeussyaapi.domain.member.exceptions;

import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;

public class UnauthenticatedEmailException extends RuntimeException {
    public UnauthenticatedEmailException() {
        super(ExceptionMessage.UN_AUTHENTICATED_EMAIL);
    }

}
