package com.sproutt.eussyaeussyaapi.domain.member;

import java.util.Arrays;

public enum Provider {
    LOCAL("local"),
    GITHUB("github");

    private final String name;

    Provider(String name) {
        this.name = name;
    }

    public boolean equals(String provider) {
        return provider.equals(this.name);
    }
}
