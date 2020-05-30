package com.sproutt.eussyaeussyaapi.domain.project.exceptions;

import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;

public class NoSuchProjectException extends RuntimeException {


    public NoSuchProjectException() {
        super(ExceptionMessage.NO_SUCH_PROJECT);
    }
}
