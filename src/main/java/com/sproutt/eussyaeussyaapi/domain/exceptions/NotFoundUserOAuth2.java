package com.sproutt.eussyaeussyaapi.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class NotFoundUserOAuth2 extends IllegalStateException {

    public NotFoundUserOAuth2() {
    }

    public NotFoundUserOAuth2(String message) {
        super(message);
    }

    public NotFoundUserOAuth2(String message, Throwable cause) {
        super(message, cause);
    }
}
