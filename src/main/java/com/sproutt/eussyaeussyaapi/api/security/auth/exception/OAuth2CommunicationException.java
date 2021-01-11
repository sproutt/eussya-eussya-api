package com.sproutt.eussyaeussyaapi.api.security.auth.exception;

import com.sproutt.eussyaeussyaapi.api.exception.message.OAuth2ExceptionMessage;
import org.springframework.web.client.RestClientException;

public class OAuth2CommunicationException extends RestClientException {

    public OAuth2CommunicationException() {
        super(OAuth2ExceptionMessage.OAUTH_COMMUNICATION_ERROR);
    }
}
