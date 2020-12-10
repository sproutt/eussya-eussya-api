package com.sproutt.eussyaeussyaapi.domain.mission.exceptions;

import com.sproutt.eussyaeussyaapi.api.exception.message.MissionExceptionMessage;

public class NotSatisfiedCondition extends RuntimeException {
    public NotSatisfiedCondition() {
        super(MissionExceptionMessage.NOT_SATISFIED_CONDITION);
    }
}
