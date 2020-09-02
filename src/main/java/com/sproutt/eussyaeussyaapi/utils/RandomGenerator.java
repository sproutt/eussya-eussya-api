package com.sproutt.eussyaeussyaapi.utils;

import java.util.UUID;

public class RandomGenerator {
    public static String createAuthenticationCode() {
        return UUID.randomUUID().toString();
    }
}
