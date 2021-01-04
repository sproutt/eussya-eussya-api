package com.sproutt.eussyaeussyaapi.api.security.auth.exception;

import com.sproutt.eussyaeussyaapi.utils.ExceptionMessage;
import org.springframework.web.client.RestClientException;

public class OAuth2CommunicationException extends RestClientException {

    public OAuth2CommunicationException() {
        super(ExceptionMessage.OAUTH_COMMUNICATION_ERROR);
    }
}
