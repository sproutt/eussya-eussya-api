package com.sproutt.eussyaeussyaapi.domain.member.exceptions;

import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;

public class DuplicationNickNameException extends DuplicationException {
    public DuplicationNickNameException() {
        super(ExceptionMessage.DUPLICATED_NICKNAME);
    }

    public DuplicationNickNameException(String message) {
        super(message);
    }
}
