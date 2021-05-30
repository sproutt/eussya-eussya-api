package com.sproutt.eussyaeussyaapi.domain.member.exceptions;

public class DuplicationMemberException extends DuplicationException {

    private static final String DUPLICATED_MEMBER_ID = "중복된 아이디입니다.";

    public DuplicationMemberException() {
        super(DUPLICATED_MEMBER_ID);

    }
}

