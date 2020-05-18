package com.sproutt.eussyaeussyaapi.api.exception;

import com.sproutt.eussyaeussyaapi.api.dto.ErrorResponse;
import com.sproutt.eussyaeussyaapi.api.dto.ValidateError;
import com.sproutt.eussyaeussyaapi.api.oauth2.exception.OAuth2CommunicationException;
import com.sproutt.eussyaeussyaapi.api.oauth2.exception.UnSupportedOAuth2Exception;
import com.sproutt.eussyaeussyaapi.api.security.exception.InvalidTokenException;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.DuplicationException;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.NoSuchMemberException;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.VerificationException;
import com.sproutt.eussyaeussyaapi.domain.member.exceptions.WrongPasswordException;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Optional;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final MessageSourceAccessor messageSourceAccessor;

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(
        MethodArgumentNotValidException exception) {
        log.info("handleMethodArgumentNotValidException : {}", exception);

        ErrorResponse response = new ErrorResponse();

        List<ObjectError> errors = exception.getBindingResult().getAllErrors();

        for (ObjectError error : errors) {
            FieldError fieldError = (FieldError) error;

            response.addValidateError(new ValidateError(fieldError.getField(), getErrorMessage(fieldError)));

            log.info("error code: {}", getFirstCode(fieldError));
            log.info("error arguments: {}", fieldError.getArguments());
            log.info("default message: {}", fieldError.getDefaultMessage());
            log.info("changed message: {}", getErrorMessage(fieldError));
        }

        log.info("response: {}", response.toString());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = InvalidTokenException.class)
    public ResponseEntity handleInvalidTokenException(InvalidTokenException exception) {
        log.info("handleInvalidTokenException : {}", exception);

        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(exception.getMessage());
    }

    @ExceptionHandler(value = DuplicationException.class)
    public ResponseEntity handleDuplicationException(DuplicationException exception) {
        log.info("handleDuplicationException : {}", exception);

        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(exception.getMessage());
    }

    @ExceptionHandler(value = WrongPasswordException.class)
    public ResponseEntity handleWrongPasswordException(WrongPasswordException exception) {
        log.info("handleWrongPasswordException : {}", exception);

        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(exception.getMessage());
    }

    @ExceptionHandler(value = VerificationException.class)
    public ResponseEntity handleVerificationException(VerificationException exception) {
        log.info("handleVerificationException : {}", exception);

        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(exception.getMessage());
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity handleNotFoundException(NotFoundException exception) {
        log.info("handleNotFoundException : {}", exception);

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(exception.getMessage());
    }

    @ExceptionHandler(value = NoSuchMemberException.class)
    public ResponseEntity handleNoSuchMemberException(NoSuchMemberException exception) {
        log.info("handleNoSuchMemberException : {}", exception);

        return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).body(exception.getMessage());
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

    private String getErrorMessage(FieldError fieldError) {
        Optional<String> code = getFirstCode(fieldError);

        if (code.orElse(null) == null) {
            return null;
        }

        return messageSourceAccessor.getMessage(code.get(), fieldError.getArguments(), fieldError.getDefaultMessage());
    }

    private Optional<String> getFirstCode(FieldError fieldError) {
        String[] codes = fieldError.getCodes();

        if (codes == null || codes.length == 0) {
            return Optional.empty();
        }

        return Optional.of(codes[0]);
    }
}
