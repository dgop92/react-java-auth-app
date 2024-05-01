package com.dgop92.authexample.common.exceptions;

import java.util.ArrayList;
import java.util.List;

public class InvalidInputException extends ApplicationException {

    public static String GENERIC_MESSAGE = "The input provided is invalid";

    private List<FieldError> errors;

    public InvalidInputException(String message) {
        super(message, ExceptionCode.INVALID_INPUT);
        // create empty list of FieldError
        this.errors = new ArrayList<>();
    }

    public InvalidInputException() {
        super(GENERIC_MESSAGE, ExceptionCode.INVALID_INPUT);
        this.errors = new ArrayList<>();
    }

    public InvalidInputException(Throwable cause) {
        super(GENERIC_MESSAGE, ExceptionCode.INVALID_INPUT, cause);
        this.errors = new ArrayList<>();
    }

    public InvalidInputException(List<FieldError> errors) {
        super(GENERIC_MESSAGE, ExceptionCode.INVALID_INPUT);
        this.errors = errors;
    }

    public InvalidInputException(String message, Throwable cause) {
        super(message, ExceptionCode.INVALID_INPUT, cause);
    }

    public InvalidInputException(String message, List<FieldError> errors) {
        super(message, ExceptionCode.INVALID_INPUT);
        this.errors = errors;
    }

    public InvalidInputException(String message, List<FieldError> errors, Throwable cause) {
        super(message, ExceptionCode.INVALID_INPUT, cause);
        this.errors = errors;
    }

    public List<FieldError> getErrors() {
        return errors;
    }

    public void addError(String path, String message) {
        FieldError fe = new FieldError(path, message);
        errors.add(fe);
    }
}
