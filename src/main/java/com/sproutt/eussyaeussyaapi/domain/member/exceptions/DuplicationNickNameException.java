package com.sproutt.eussyaeussyaapi.domain.member.exceptions;

import com.sproutt.eussyaeussyaapi.api.exception.message.MemberExceptionMessage;

public class DuplicationNickNameException extends DuplicationException {
    public DuplicationNickNameException() {
        super(MemberExceptionMessage.DUPLICATED_NICKNAME);
    }

    public DuplicationNickNameException(String message) {
        super(message);
    }
}
