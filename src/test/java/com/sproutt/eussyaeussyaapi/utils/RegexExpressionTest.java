package com.sproutt.eussyaeussyaapi.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

public class RegexExpressionTest {

    @Test
    @DisplayName("비밀번호 정규식 테스트(영문, 숫자, 특수문자 중 두 가지 이상 포함 6~15자리)")
    void test() {
        String onlyDigits = "1234567";
        String onlyAlphabet = "abcdevfd";
        String lengthsUnder6 = "123a!";
        String password = "12345aA!";

        assertThat(password).matches(RegexExpression.PASSWORD);

        boolean result = Pattern.matches(RegexExpression.PASSWORD, onlyDigits);
        assertThat(result).isFalse();

        result = Pattern.matches(RegexExpression.PASSWORD, onlyAlphabet);
        assertThat(result).isFalse();

        result = Pattern.matches(RegexExpression.PASSWORD, lengthsUnder6);
        assertThat(result).isFalse();

    }
}
