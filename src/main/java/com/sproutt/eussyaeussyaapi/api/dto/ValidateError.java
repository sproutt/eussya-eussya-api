package com.sproutt.eussyaeussyaapi.api.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateError {

    private String fieldName;
    private String errorMessage;

    public ValidateError(String fieldName, String errorMessage) {
        this.fieldName = fieldName;
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "ValidateError{" +
                "fieldName='" + fieldName + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
