package com.dgop92.authexample.common.rest;

import com.dgop92.authexample.common.exceptions.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = ApplicationException.class)
    public ResponseEntity<ApiError> handleApplicationException(HttpServletRequest req, ApplicationException ex){

        HttpStatus statusCode = ErrorCodeUtils.getHttpStatusFromExceptionCode(ex.getExceptionCode());
        String userMessage = ex.getMessage();
        String path = req.getRequestURI();

        ApiError apiError = new ApiError(statusCode, path, userMessage, ex);


        return ResponseEntity.status(statusCode).body(apiError);
    }

    @ExceptionHandler(value = InvalidInputException.class)
    public ResponseEntity<ApiError> handleInvalidInputException(HttpServletRequest req, InvalidInputException ex){

        HttpStatus statusCode = ErrorCodeUtils.getHttpStatusFromExceptionCode(ex.getExceptionCode());
        String userMessage = ex.getMessage();
        List<FieldError> fieldErrors = ex.getErrors();
        String path = req.getRequestURI();

        ApiError apiError = new ApiError(statusCode, path, userMessage, ex, fieldErrors);

        return ResponseEntity.status(statusCode).body(apiError);
    }

    @ExceptionHandler(value = RepositoryException.class)
    public ResponseEntity<ApiError> handleRepositoryException(HttpServletRequest req, RepositoryException ex){

        HttpStatus statusCode = ErrorCodeUtils.getHttpStatusFromExceptionCode(ex.getExceptionCode());
        String userMessage = ex.getMessage();
        String path = req.getRequestURI();

        ApiError apiError = new ApiError(statusCode, path, userMessage, ex);

        return ResponseEntity.status(statusCode).body(apiError);
    }

    @ExceptionHandler(value = PresentationException.class)
    public ResponseEntity<ApiError> handlePresentationException(HttpServletRequest req, PresentationException ex){

        HttpStatus statusCode = ErrorCodeUtils.getHttpStatusFromExceptionCode(ex.getExceptionCode());
        String userMessage = ex.getMessage();
        String path = req.getRequestURI();

        ApiError apiError = new ApiError(statusCode, path, userMessage, ex);

        return ResponseEntity.status(statusCode).body(apiError);
    }
}
