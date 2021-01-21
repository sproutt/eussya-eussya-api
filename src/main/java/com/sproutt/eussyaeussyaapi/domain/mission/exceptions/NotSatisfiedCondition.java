package com.sproutt.eussyaeussyaapi.domain.mission.exceptions;

public class NotSatisfiedCondition extends RuntimeException {

    private static final String NOT_SATISFIED_CONDITION = "목표 시간을 채우지 못했습니다.";

    public NotSatisfiedCondition() {
        super(NOT_SATISFIED_CONDITION);
    }
}
