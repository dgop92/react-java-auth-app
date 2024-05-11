package com.dgop92.authexample.common.web;

import com.dgop92.authexample.common.exceptions.FieldError;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ApiError {

    private final HttpStatus status;
    private final String path;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private final LocalDateTime timestamp;
    private String message;
    private String debugMessage;
    private List<FieldError> fieldErrors;

    ApiError(HttpStatus status, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.path = path;
        this.message = "unexpected error";
        this.debugMessage = "unexpected error";
        this.fieldErrors = List.of();
    }

    ApiError(HttpStatus status, String path, Throwable ex) {
        this(status, path);
        this.message = "unexpected error";
        this.debugMessage = ex.getLocalizedMessage();
    }

    ApiError(HttpStatus status, String path, String message, Throwable ex) {
        this(status, path);
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
    }

    ApiError(HttpStatus status, String path, String message, Throwable ex, List<FieldError> fieldErrors) {
        this(status, path);
        this.message = message;
        this.debugMessage = ex.getLocalizedMessage();
        this.fieldErrors = fieldErrors;
    }
}
