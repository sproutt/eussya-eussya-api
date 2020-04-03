package com.sproutt.eussyaeussyaapi.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RandomGeneratorTest {

    @Test
    @DisplayName("인증 코드 생성 테스트(8자리 랜덤 문자열)")
    void createAuthenticationCode() {
        String authCode = RandomGenerator.createAuthenticationCode();

        assertThat(authCode.length()).isEqualTo(8);
    }
}