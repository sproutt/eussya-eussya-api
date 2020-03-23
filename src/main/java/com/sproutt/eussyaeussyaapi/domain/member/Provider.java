package com.sproutt.eussyaeussyaapi.domain.member;

public enum Provider {
    LOCAL("local"),
    FACEBOOK("facebook"),
    GOOGLE("google"),
    GITHUB("github");

    private final String name;

    Provider(String name) {
        this.name = name;
    }
}
