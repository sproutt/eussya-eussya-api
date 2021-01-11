package com.sproutt.eussyaeussyaapi.api.security.auth.exception;

import com.sproutt.eussyaeussyaapi.api.exception.message.OAuth2ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UnSupportedOAuth2Exception extends RuntimeException{

    public UnSupportedOAuth2Exception(){
        super(OAuth2ExceptionMessage.UNSUPPORT_OAUTH);
    }
}
