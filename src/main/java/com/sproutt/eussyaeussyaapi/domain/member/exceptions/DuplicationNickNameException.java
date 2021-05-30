package com.sproutt.eussyaeussyaapi.domain.member.exceptions;

public class DuplicationNickNameException extends DuplicationException {

    private static final String DUPLICATED_NICKNAME = "중복된 닉네임입니다.";

    public DuplicationNickNameException() {
        super(DUPLICATED_NICKNAME);
    }
}
