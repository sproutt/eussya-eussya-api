package com.sproutt.eussyaeussyaapi.domain.member.exceptions;

public class NoSuchMemberException extends RuntimeException {

    private static final String NO_SUCH_MEMBER = "존재하지 않는 아이디입니다.";

    public NoSuchMemberException() {
        super(NO_SUCH_MEMBER);
    }
}
