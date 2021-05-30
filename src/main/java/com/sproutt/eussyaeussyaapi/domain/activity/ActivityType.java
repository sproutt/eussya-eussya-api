package com.sproutt.eussyaeussyaapi.domain.activity;

public enum ActivityType {
    ULDDO("01", "얼또"),
    GROUP_TIL("02", "그룹TIL");

    private final String code;
    private final String description;

    ActivityType(String code, String description) {
        this.code = code;
        this.description = description;
    }
}
