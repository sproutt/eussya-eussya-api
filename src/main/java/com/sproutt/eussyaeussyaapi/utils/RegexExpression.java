package com.sproutt.eussyaeussyaapi.utils;

public class RegexExpression {

    // 대문자, 소문자, 숫자, 특수문자 포함 8~15자
    public static final String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,15}";
}
