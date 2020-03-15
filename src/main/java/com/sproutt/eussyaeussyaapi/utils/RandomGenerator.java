package com.sproutt.eussyaeussyaapi.utils;

import java.util.Random;

public class RandomGenerator {
    private static final int AUTHENTICATION_CODE_LENGTH = 8;
    private static final char[] CHARSET = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
            'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
            'Y', 'Z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};

    public static String createAuthenticationCode() {
        StringBuilder code = new StringBuilder();
        Random random = new Random(System.currentTimeMillis());

        while (code.length() != AUTHENTICATION_CODE_LENGTH) {
            code.append(CHARSET[random.nextInt(CHARSET.length)]);
        }

        return code.toString();
    }
}
