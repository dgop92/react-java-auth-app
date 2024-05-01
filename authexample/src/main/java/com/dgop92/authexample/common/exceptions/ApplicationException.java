package com.dgop92.authexample.common.exceptions;

public class ApplicationException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public ApplicationException(String message, ExceptionCode exceptionCode) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public ApplicationException(String message, ExceptionCode exceptionCode, Throwable cause) {
        super(message, cause);
        this.exceptionCode = exceptionCode;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }
}
