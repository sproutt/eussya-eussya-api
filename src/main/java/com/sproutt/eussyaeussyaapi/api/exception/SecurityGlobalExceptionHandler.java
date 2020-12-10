package com.sproutt.eussyaeussyaapi.api.exception;

import com.sproutt.eussyaeussyaapi.api.exception.dto.ErrorCode;
import com.sproutt.eussyaeussyaapi.api.exception.dto.ErrorResponse;
import com.sproutt.eussyaeussyaapi.api.security.exception.InvalidTokenException;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class SecurityGlobalExceptionHandler extends GlobalExceptionHandler {

    public SecurityGlobalExceptionHandler(MessageSourceAccessor messageSourceAccessor) {
        super(messageSourceAccessor);
    }

    @ExceptionHandler(value = InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException exception) {
        log.info("handleInvalidTokenException : {}", exception);

        ErrorResponse response = new ErrorResponse();
        response.of(ErrorCode.INVALID_TOKEN);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON).body(response);
    }
}
