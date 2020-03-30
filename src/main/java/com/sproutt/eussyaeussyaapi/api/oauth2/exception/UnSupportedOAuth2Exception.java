package com.sproutt.eussyaeussyaapi.api.oauth2.exception;

import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class UnSupportedOAuth2Exception extends RuntimeException{

    public UnSupportedOAuth2Exception(){
        super(ExceptionMessage.UNSUPPORT_OAUTH);
    }
}
