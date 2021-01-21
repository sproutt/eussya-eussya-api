package com.sproutt.eussyaeussyaapi.domain.member.exceptions;

public class WrongPasswordException extends RuntimeException {

    private static final String WRONG_PASSWORD = "잘못된 비밀번호 입니다.";

    public WrongPasswordException() {
        super(WRONG_PASSWORD);
    }
}
