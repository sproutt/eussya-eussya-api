package com.sproutt.eussyaeussyaapi.domain.member.exceptions;

import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;

public class NotExistMemberException extends RuntimeException {
    // TODO Exception 레거시 작업 시 NoSuchMemberException와 합쳐야 함
    public NotExistMemberException() {
        super(ExceptionMessage.NO_SUCH_MEMBER);
    }

}
