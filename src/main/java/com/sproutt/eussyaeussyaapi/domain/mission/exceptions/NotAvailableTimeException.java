package com.sproutt.eussyaeussyaapi.domain.mission.exceptions;


import com.sproutt.eussyaeussyaapi.api.exception.message.MissionExceptionMessage;

public class NotAvailableTimeException extends RuntimeException {
    public NotAvailableTimeException() {
        super(MissionExceptionMessage.NOT_AVAILABLE_TIME);
    }
}
