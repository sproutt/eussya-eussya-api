package com.sproutt.eussyaeussyaapi.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DuplicatedMemberIdException extends RuntimeException {
    public DuplicatedMemberIdException() {
        super("중복된 아이디입니다.");
    }

    public DuplicatedMemberIdException(String message) {
        super(message);
    }
}
