package com.sproutt.eussyaeussyaapi.api.exception;

import com.sproutt.eussyaeussyaapi.api.security.auth.exception.OAuth2CommunicationException;
import com.sproutt.eussyaeussyaapi.api.security.auth.exception.UnSupportedOAuth2Exception;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OAuth2GlobalExceptionHandler extends GlobalExceptionHandler {

    public OAuth2GlobalExceptionHandler(MessageSourceAccessor messageSourceAccessor) {
        super(messageSourceAccessor);
    }

    @ExceptionHandler(value = OAuth2CommunicationException.class)
    public ResponseEntity handleOAuth2CommunicationException(OAuth2CommunicationException exception) {
        log.info("handleOAuth2CommunicationException : {}", exception);

        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(exception.getMessage());
    }

    @ExceptionHandler(value = UnSupportedOAuth2Exception.class)
    public ResponseEntity handleUnSupportOAuth2Exception(UnSupportedOAuth2Exception exception) {
        log.info("handleUnSupportedOAuth2Exception : {}", exception);

        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(exception.getMessage());
    }
}
