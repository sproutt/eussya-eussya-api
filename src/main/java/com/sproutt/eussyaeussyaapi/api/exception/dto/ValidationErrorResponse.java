package com.sproutt.eussyaeussyaapi.api.exception.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationErrorResponse extends ErrorResponse {
    private List<ValidateError> errors;

    public ValidationErrorResponse() {
        this.errors = new ArrayList<>();
    }

    public void addValidateError(ValidateError error) {
        this.errors.add(error);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ValidationErrorResponse{")
                     .append("ErrorResponse{")
                     .append("code=")
                     .append(getCode())
                     .append(", message=")
                     .append(getMessage())
                     .append("}, errors={");

        errors.forEach(error -> stringBuilder.append(error.toString()).append(", "));
        stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "");
        stringBuilder.append("}");

        return stringBuilder.toString();
    }
}
