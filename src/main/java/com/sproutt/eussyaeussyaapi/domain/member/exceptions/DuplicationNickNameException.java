package com.sproutt.eussyaeussyaapi.domain.member.exceptions;

import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;

public class DuplicationNickNameException extends RuntimeException {
    public DuplicationNickNameException() {
        super(ExceptionMessage.DUPLICATED_NICKNAME);
    }
}
