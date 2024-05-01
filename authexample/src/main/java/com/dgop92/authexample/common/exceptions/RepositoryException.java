package com.dgop92.authexample.common.exceptions;

public class RepositoryException extends RuntimeException {

    private final ExceptionCode exceptionCode;

    public RepositoryException(String message, ExceptionCode exceptionCode) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    public RepositoryException(String message, ExceptionCode exceptionCode, Throwable cause) {
        super(message, cause);
        this.exceptionCode = exceptionCode;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }
}
