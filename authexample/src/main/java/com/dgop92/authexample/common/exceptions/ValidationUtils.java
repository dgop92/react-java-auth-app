package com.dgop92.authexample.common.exceptions;

import jakarta.validation.Validator;


public class ValidationUtils {


    public static <T> void validate(Validator validator, T input){
        var valResult = validator.validate(input);
        if (!valResult.isEmpty()) {
            InvalidInputException ex = new InvalidInputException();
            valResult.forEach(vr -> ex.addError(vr.getPropertyPath().toString(), vr.getMessage()));
            throw ex;
        }
    }
}
