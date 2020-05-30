package com.sproutt.eussyaeussyaapi.domain.project.exceptions;

import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;

public class NotAuthorizedException extends RuntimeException {

    public NotAuthorizedException() {
        super(ExceptionMessage.NOT_AUTHORIZED);
    }
}
