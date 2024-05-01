package com.dgop92.authexample.common.rest;

import com.dgop92.authexample.common.exceptions.ExceptionCode;
import org.springframework.http.HttpStatus;

public class ErrorCodeUtils {

    public static HttpStatus getHttpStatusFromExceptionCode(ExceptionCode exceptionCode) {
        return switch (exceptionCode) {
            case INVALID_INPUT, INVALID_OPERATION, INVALID_ID, ID_NOT_PROVIDED -> HttpStatus.BAD_REQUEST;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case DUPLICATED_RECORD -> HttpStatus.CONFLICT;
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
    }
}
