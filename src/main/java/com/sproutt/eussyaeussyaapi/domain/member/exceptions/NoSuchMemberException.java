package com.sproutt.eussyaeussyaapi.domain.member.exceptions;

import com.sproutt.eussyaeussyaapi.api.exception.message.MemberExceptionMessage;

public class NoSuchMemberException extends RuntimeException {
    public NoSuchMemberException() {
        super(MemberExceptionMessage.NO_SUCH_MEMBER);
    }
}
