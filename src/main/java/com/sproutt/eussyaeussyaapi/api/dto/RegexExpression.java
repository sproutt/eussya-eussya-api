package com.sproutt.eussyaeussyaapi.api.dto;

public class RegexExpression {

    // 8~15자, 대문자 1자리 소문자 1자리 숫자 1자리 특수문자 1자리
    public static final String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,15}";
}
