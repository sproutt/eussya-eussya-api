package com.sproutt.eussyaeussyaapi.api.security.auth.exception;

import org.springframework.web.client.RestClientException;

public class OAuth2CommunicationException extends RestClientException {

    private static final String OAUTH_COMMUNICATION_ERROR = "oauth 통신 중 오류가 발생하였습니다.";

    public OAuth2CommunicationException() {
        super(OAUTH_COMMUNICATION_ERROR);
    }
}
