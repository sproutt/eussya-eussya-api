package com.sproutt.eussyaeussyaapi.api.exception.dto;

import lombok.Getter;

@Getter
public enum ErrorCode {

    INVALID_TOKEN("AUT001", "유효하지 않은 토큰입니다."),
    UN_AUTHENTICATED_EMAIL("AUT002", "email 인증이 되지 않은 계정입니다."),
    WRONG_DEADLINE_TIME("MIS001", "목표 종료 시간은 03 ~ 09시 사이로 설정해야합니다."),
    INVALID_INPUT_VALUE("INVALID_INPUT", "잘못된 입력입니다.");

    private String code;
    private String message;

    ErrorCode(String code, String message) {
        this.message = message;
        this.code = code;
    }
}
