package com.sproutt.eussyaeussyaapi.api.oauth2.exception;

import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;

public class UnSupportOAuth2Exception extends RuntimeException{

    public UnSupportOAuth2Exception(){
        super(ExceptionMessage.UNSUPPORT_OAUTH);
    }

}
