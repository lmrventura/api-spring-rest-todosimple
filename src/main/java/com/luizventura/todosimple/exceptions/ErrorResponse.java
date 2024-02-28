package com.luizventura.todosimple.exceptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor //for the variables with final. for the other ones (stackTrace, errors) we can just make a set...
@JsonInclude(JsonInclude.Include.NON_NULL) // for stacktrace and errros when server.error.include-exception=false
public class ErrorResponse {
    private final int status;
    private final String message;
    private String stackTrace;
    private List<ValidationError> errors;

    @Getter
    @Setter
    @RequiredArgsConstructor
    private static class ValidationError { //para dados estáticos, então os dados já vão ser iniciados com ela;
        private final String field;
        private final String message;
    }

    public void addValidationError(String field, String message) {
        if (Objects.isNull(errors)) {
            this.errors = new ArrayList<>(); //this is to avoid memory error in case "private List<ValidationError> errors;" is not initialized.
        }
        this.errors.add(new ValidationError(field, message));
    }
}
