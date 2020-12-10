package com.sproutt.eussyaeussyaapi.domain.member.exceptions;

import com.sproutt.eussyaeussyaapi.api.exception.message.MemberExceptionMessage;

public class UnauthenticatedEmailException extends RuntimeException {
    public UnauthenticatedEmailException() {
        super(MemberExceptionMessage.UN_AUTHENTICATED_EMAIL);
    }

}
