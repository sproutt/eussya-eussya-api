package com.sproutt.eussyaeussyaapi.utils;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class RandomGeneratorTest {

    @Test
    void createAuthenticationCode() {
        String authCode = RandomGenerator.createAuthenticationCode();

        assertThat(authCode.length()).isEqualTo(8);
    }
}