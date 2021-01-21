package com.sproutt.eussyaeussyaapi.domain.mission.exceptions;

public class LimitedTimeExceedException extends RuntimeException {

    private static final String LIMITED_TIME_EXCEED = "미션 완료 제한 시간을 초과하였습니다.";

    public LimitedTimeExceedException() {
        super(LIMITED_TIME_EXCEED);
    }
}
