package com.sproutt.eussyaeussyaapi.domain.mission.exceptions;


public class NotAvailableTimeException extends RuntimeException {

    public static final String NOT_AVAILABLE_TIME = "서비스 시간이 아닙니다.";

    public NotAvailableTimeException() {
        super(NOT_AVAILABLE_TIME);
    }
}
