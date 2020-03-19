package com.sproutt.eussyaeussyaapi.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ErrorResponse {

    private List<ValidateError> errors;

    public ErrorResponse() {
        this.errors = new ArrayList<>();
    }

    public void addValidateError(ValidateError error) {
        this.errors.add(error);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ErrorResponse{")
                     .append("errors={");

        errors.forEach(error -> stringBuilder.append(error.toString()).append(", "));
        stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length(), "");
        stringBuilder.append("}");

        return stringBuilder.toString();
    }
}
