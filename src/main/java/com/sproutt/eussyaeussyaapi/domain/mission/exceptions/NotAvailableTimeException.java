package com.sproutt.eussyaeussyaapi.domain.mission.exceptions;


import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;

public class NotAvailableTimeException extends RuntimeException {
    public NotAvailableTimeException() {
        super(ExceptionMessage.NOT_AVAILABLE_TIME);
    }
}
