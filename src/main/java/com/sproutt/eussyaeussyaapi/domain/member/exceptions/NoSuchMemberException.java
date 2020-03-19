package com.sproutt.eussyaeussyaapi.domain.member.exceptions;

import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;

public class NoSuchMemberException extends RuntimeException {
    public NoSuchMemberException() {
        super(ExceptionMessage.NO_SUCH_MEMBER);
    }
}
