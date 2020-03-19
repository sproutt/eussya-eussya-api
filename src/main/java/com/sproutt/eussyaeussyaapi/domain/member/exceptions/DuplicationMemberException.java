package com.sproutt.eussyaeussyaapi.domain.member.exceptions;

import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DuplicationMemberException extends RuntimeException {
    public DuplicationMemberException() {
        super(ExceptionMessage.DUPLICATED_MEMBER_ID);
    }

    public DuplicationMemberException(String message) {
        super(message);
    }
}
