package com.sproutt.eussyaeussyaapi.utils.exception;

public class NotImplementedException extends RuntimeException {
    public NotImplementedException() {
        super("구현되지 않은 기능");
    }
}
