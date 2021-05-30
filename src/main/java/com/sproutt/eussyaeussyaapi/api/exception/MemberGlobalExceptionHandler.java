package com.sproutt.eussyaeussyaapi.api.exception;

import com.sproutt.eussyaeussyaapi.api.exception.dto.ErrorCode;
import com.sproutt.eussyaeussyaapi.api.exception.dto.ErrorResponse;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.*;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MemberGlobalExceptionHandler extends GlobalExceptionHandler {

    public MemberGlobalExceptionHandler(MessageSourceAccessor messageSourceAccessor) {
        super(messageSourceAccessor);
    }


    @ExceptionHandler(value = VerificationException.class)
    public ResponseEntity handleVerificationException(VerificationException exception) {
        log.info("handleVerificationException : {}", exception);

        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(exception.getMessage());
    }

    @ExceptionHandler(value = DuplicationException.class)
    public ResponseEntity handleDuplicationException(DuplicationException exception) {
        log.info("handleDuplicationException : {}", exception);

        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(exception.getMessage());
    }

    @ExceptionHandler(value = UnauthenticatedEmailException.class)
    public ResponseEntity<ErrorResponse> handleUnauthenticatedEmailException(UnauthenticatedEmailException exception) {
        log.info("UnauthenticatedEmailException : {}", exception);

        ErrorResponse response = new ErrorResponse();
        response.of(ErrorCode.UN_AUTHENTICATED_EMAIL);

        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(response);
    }

    @ExceptionHandler(value = WrongPasswordException.class)
    public ResponseEntity handleWrongPasswordException(WrongPasswordException exception) {
        log.info("handleWrongPasswordException : {}", exception);

        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(exception.getMessage());
    }

    @ExceptionHandler(value = NoSuchMemberException.class)
    public ResponseEntity handleNoSuchMemberException(NoSuchMemberException exception) {
        log.info("handleNoSuchMemberException : {}", exception);

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(exception.getMessage());
    }
}
