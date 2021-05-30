package com.sproutt.eussyaeussyaapi.api.exception;

import com.sproutt.eussyaeussyaapi.domain.mission.exceptions.*;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MissionGlobalExceptionHandler extends GlobalExceptionHandler {

    public MissionGlobalExceptionHandler(MessageSourceAccessor messageSourceAccessor) {
        super(messageSourceAccessor);
    }

    @ExceptionHandler(value = NotSatisfiedCondition.class)
    public ResponseEntity NotSatisfiedCondition(NotSatisfiedCondition exception) {
        log.info("NotSatisfiedCondition : {}", exception);

        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(exception.getMessage());
    }

    @ExceptionHandler(value = ExpiredMissionException.class)
    public ResponseEntity ExpiredMissionException(ExpiredMissionException exception) {
        log.info("ExpiredMissionException : {}", exception);

        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(exception.getMessage());
    }

    @ExceptionHandler(value = NoPermissionException.class)
    public ResponseEntity NoPermissionException(NoPermissionException exception) {
        log.info("NoPermissionException : {}", exception);

        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(exception.getMessage());
    }

    @ExceptionHandler(value = NotAvailableTimeException.class)
    public ResponseEntity notAvailableTimeException(NotAvailableTimeException exception) {
        log.info("notAvailableTimeException : {}", exception);

        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(exception.getMessage());
    }

    @ExceptionHandler(value = NoSuchMissionException.class)
    public ResponseEntity noSuchMissionException(NoSuchMissionException exception) {
        log.info("NoSuchMissionException : {}", exception);

        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(exception.getMessage());
    }

    @ExceptionHandler(value = NotCompletedMissionException.class)
    public ResponseEntity handleNotCompletedMissionException(NotCompletedMissionException exception) {
        log.info("NotCompletedMissionException : {}", exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(exception.getMessage());
    }

    @ExceptionHandler(value = CompletedMissionException.class)
    public ResponseEntity handleCompletedMissionException(CompletedMissionException exception) {
        log.info("CompletedMissionException : {}", exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(exception.getMessage());
    }

    @ExceptionHandler(value = LimitedTimeExceedException.class)
    public ResponseEntity handleLimitedTimeExceedException(LimitedTimeExceedException exception) {
        log.info("LimitedTimeExceedException : {}", exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(exception.getMessage());
    }
}
