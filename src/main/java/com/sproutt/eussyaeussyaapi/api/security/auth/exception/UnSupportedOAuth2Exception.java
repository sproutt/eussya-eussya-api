package com.sproutt.eussyaeussyaapi.api.security.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UnSupportedOAuth2Exception extends RuntimeException {

    private static final String UNSUPPORT_OAUTH = "지원하지 않거나 존재하지 않는 oauth 입니다.";

    public UnSupportedOAuth2Exception() {
        super(UNSUPPORT_OAUTH);
    }
}
