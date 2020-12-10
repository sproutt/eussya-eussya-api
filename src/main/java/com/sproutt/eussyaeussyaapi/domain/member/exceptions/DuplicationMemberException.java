package com.sproutt.eussyaeussyaapi.domain.member.exceptions;

import com.sproutt.eussyaeussyaapi.api.exception.message.MemberExceptionMessage;

public class DuplicationMemberException extends DuplicationException {
    public DuplicationMemberException() {
        super(MemberExceptionMessage.DUPLICATED_MEMBER_ID);
    }

    public DuplicationMemberException(String message) {
        super(message);
    }
}
