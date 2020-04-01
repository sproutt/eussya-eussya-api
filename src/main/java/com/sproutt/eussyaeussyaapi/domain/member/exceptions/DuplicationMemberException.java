package com.sproutt.eussyaeussyaapi.domain.member.exceptions;

import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;

public class DuplicationMemberException extends DuplicationException {
    public DuplicationMemberException() {
        super(ExceptionMessage.DUPLICATED_MEMBER_ID);
    }

    public DuplicationMemberException(String message) {
        super(message);
    }
}
