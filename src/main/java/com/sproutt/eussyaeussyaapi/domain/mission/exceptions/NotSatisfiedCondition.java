package com.sproutt.eussyaeussyaapi.domain.mission.exceptions;

import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;

public class NotSatisfiedCondition extends RuntimeException {
    public NotSatisfiedCondition() {
        super(ExceptionMessage.NOT_SATISFIED_CONDITION);
    }
}
