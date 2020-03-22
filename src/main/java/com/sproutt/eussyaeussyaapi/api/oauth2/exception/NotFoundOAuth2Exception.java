package com.sproutt.eussyaeussyaapi.api.oauth2.exception;

import com.sproutt.eussyaeussyaapi.domain.member.exceptions.NoSuchMemberException;

public class NotFoundOAuth2Exception extends NoSuchMemberException {

    public NotFoundOAuth2Exception(){
        super();
    }
}
